package com.example.lunit.controller;

import com.example.lunit.dto.account.InfoForm;
import com.example.lunit.dto.account.LoginRequest;
import com.example.lunit.dto.account.PasswordForm;
import com.example.lunit.dto.account.SignUpForm;
import com.example.lunit.model.Account;
import com.example.lunit.repository.AccountRepository;
import com.example.lunit.repository.ImageReportRepository;
import com.example.lunit.repository.ImageRepository;
import com.example.lunit.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageReportRepository imageReportRepository;

    @Autowired
    AccountService accountService;



    @Test
    @WithMockUser
    @DisplayName("???????????? ??????")
    void test_modify_password() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        PasswordForm passwordForm = new PasswordForm();
        passwordForm.setPassword("sssskkkk");

        mockMvc.perform(post("/auth/setting/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordForm)))
                .andExpect(status().isOk())
                .andDo(document("modify-pw",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("????????? ????????????")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("?????? ??????")
    void test_modify_user_info() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sssskkkk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        InfoForm infoForm = new InfoForm();

        infoForm.setAddress("???????????????"); infoForm.setTelephone("01012345678");

        mockMvc.perform(post("/auth/setting/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(infoForm)))
                .andExpect(status().isOk())
                .andDo(document("modify-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("address").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("telephone").type(JsonFieldType.STRING).description("??????")
                        )
                ));

        // then

        Account account = accountRepository.findAccountByEmail("test1@email.com");
        assertEquals(account.getAddress(), "???????????????");
        assertEquals(account.getTelephone(), "01012345678");
    }

    @Test
    @WithMockUser
    @DisplayName("?????? ?????????")
    void test_delete_account() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("skdisk17");

        Account account = accountRepository.findAccountByEmail("test@email.com");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        mockMvc.perform(post("/auth/delete"))
                .andExpect(status().isOk())
                .andDo(document("delete-account",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        // then
        assertFalse(imageRepository.existsByAccount(account));
        assertFalse(imageReportRepository.existsByAccount(account));
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void test_new_sign_up() throws Exception {
        SignUpForm signUpForm = new SignUpForm();

        String email = "test3@email.com";
        String name = "?????????";

        signUpForm.setEmail(email);
        signUpForm.setName(name);
        signUpForm.setPassword("sksksksk");
        signUpForm.setAddress("?????????");
        signUpForm.setTelephone("01049212372");

        mockMvc.perform(post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpForm)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("sign-up",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("password"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("??????").optional(),
                                fieldWithPath("telephone").type(JsonFieldType.STRING).description("?????????").optional()
                        ),
                        responseFields(
                                fieldWithPath("message").description("?????? ?????????")
                        )
                ));

        assertTrue(accountRepository.existsByEmail(email));
        Account account = accountRepository.findAccountByEmail(email);
        assertEquals(account.getName(), name);
    }

    @Test
    @WithMockUser
    @DisplayName("?????? ?????? ?????? ?????????")
    void test_get_user_info() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        mockMvc.perform(get("/auth/info"))
                .andDo(print())
                .andDo(document("user-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("user id"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("signUpDate").type(JsonFieldType.STRING).description("?????????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ????????? - ??????")
    @WithMockUser
    void test_login() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("test1@email.com"))
                .andDo(document("login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("grantType").type(JsonFieldType.STRING).description("barear"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("access token"),
                                fieldWithPath("accessTokenExpiresIn").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("refresh token")
                        )
                ));
    }
}