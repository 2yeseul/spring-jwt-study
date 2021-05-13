package com.example.lunit.service;

import com.example.lunit.config.images.ImageUploader;
import com.example.lunit.config.jwt.TokenProvider;
import com.example.lunit.dto.account.*;
import com.example.lunit.dto.jwt.TokenDto;
import com.example.lunit.model.Account;
import com.example.lunit.model.Images;
import com.example.lunit.model.RefreshToken;
import com.example.lunit.repository.AccountRepository;
import com.example.lunit.model.UserAccount;
import com.example.lunit.repository.ImageRepository;
import com.example.lunit.repository.RefreshTokenRepository;
import com.example.lunit.utils.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public Account getUserInfo() {
        return accountRepository.findAccountByEmail(SecurityUtil.getUserName());
    }

    // 사용자 정보 반환
    public UserProfile getUserProfile(Account account) {
        return UserProfile.of(account);
    }

    // 로그인
    @Transactional
    public TokenDto login(LoginRequest loginRequest) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();

        // 2. 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .email(authentication.getName())
                .tokenValue(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 5. 토큰 발급
        return tokenDto;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByEmail(username);

        if(account == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserAccount(account);
    }

    // 회원가입
    @Transactional
    public SignUpResponse signUpProcess(SignUpForm signUpForm) {
        if(accountRepository.existsByEmail(signUpForm.getEmail())) {
            return SignUpResponse.builder()
                    .message("fail - email is already exists")
                    .build();
        }

        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .name(signUpForm.getName())
                .signUpDate(LocalDate.now())
                .address(signUpForm.getAddress())
                .telephone(signUpForm.getTelephone())
                .build();

        accountRepository.save(account);
        return SignUpResponse.builder()
                .message("success")
                .build();
    }

    // 비밀번호 변경 form
    @Transactional
    public void modifyPasswordProcess(Account account, PasswordForm passwordForm) {
        account.modifyPassword(passwordEncoder.encode(passwordForm.getPassword()));
    }

    // 개인 정보 수정
    @Transactional
    public void modifyPersonalInfo(Account account, InfoForm infoForm) {
        account.modifyInfos(infoForm);
    }

    @Transactional
    public void deleteAccount(Account account) {
        // s3 파일 삭제
        if(imageRepository.existsByAccount(account)) {
            deleteImagesInS3(account);
            accountRepository.delete(account);
        }
    }

    private void deleteImagesInS3(Account account) {
        List<Images> imagesList = imageRepository.findAllByAccount(account);
        for (Images images : imagesList) {
            imageService.deleteImage(account, images.getId());
        }
    }
}
