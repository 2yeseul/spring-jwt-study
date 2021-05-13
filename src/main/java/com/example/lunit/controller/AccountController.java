package com.example.lunit.controller;

import com.example.lunit.dto.account.*;
import com.example.lunit.dto.jwt.TokenDto;
import com.example.lunit.model.Account;
import com.example.lunit.model.CurrentUser;
import com.example.lunit.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AccountController {
    private final AccountService accountService;

    // 사용자 정보 반환
    @GetMapping("/info")
    public UserProfile getUserProfile(@CurrentUser Account account) {
        return accountService.getUserProfile(account);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(accountService.login(loginRequest));
    }

    // 회원가입
    @PostMapping("/sign-up")
    public SignUpResponse signUp(@RequestBody SignUpForm signUpForm) {
        return accountService.signUpProcess(signUpForm);
    }

    // 정보 수정
    @PostMapping("/setting/info")
    public void modifyUserInformation(@CurrentUser Account account, @RequestBody InfoForm infoForm) {
        accountService.modifyPersonalInfo(account, infoForm);
    }

    // 비밀번호 변경
    @PostMapping("/setting/password")
    public void modifyPassword(@CurrentUser Account account, @RequestBody PasswordForm passwordForm) {
        accountService.modifyPasswordProcess(account, passwordForm);
    }

    // 탈퇴
    @PostMapping("/delete")
    public void deleteAccount(@CurrentUser Account account) {
        accountService.deleteAccount(account);
    }
}
