package com.example.lunit.controller;

import com.example.lunit.dto.account.LoginRequest;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
class ImageReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("????????? ???????????? ?????? ?????? ?????? ??????")
    void test_get_all_results() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        mockMvc.perform(get("/api/analyze/result/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andDo(document("analyze-all-results",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].reportId").type(JsonFieldType.NUMBER).description("report id"),
                                fieldWithPath("[].decision").type(JsonFieldType.BOOLEAN).description("Decision"),
                                fieldWithPath("[].scopeIoStart").type(JsonFieldType.NUMBER).description("SCOPE IO ?????? ???"),
                                fieldWithPath("[].scopeIoEnd").type(JsonFieldType.NUMBER).description("SCOPE IO ??? ???"),
                                fieldWithPath("[].cutOffStart").type(JsonFieldType.NUMBER).description("Cut Off ?????? ???"),
                                fieldWithPath("[].cutOffEnd").type(JsonFieldType.NUMBER).description("SCOPE IO ??? ???"),
                                fieldWithPath("[].reportDate").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("[].gridResults[].i_min").type(JsonFieldType.NUMBER).description("Intratumoral TIL min"),
                                fieldWithPath("[].gridResults[].i_avg").type(JsonFieldType.NUMBER).description("Intratumoral TIL avg"),
                                fieldWithPath("[].gridResults[].i_max").type(JsonFieldType.NUMBER).description("Intratumoral TIL max"),
                                fieldWithPath("[].gridResults[].s_min").type(JsonFieldType.NUMBER).description("Stromal TIL min"),
                                fieldWithPath("[].gridResults[].s_avg").type(JsonFieldType.NUMBER).description("Stromal TIL avg"),
                                fieldWithPath("[].gridResults[].s_max").type(JsonFieldType.NUMBER).description("Stromal TIL max")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("???????????? ?????? ?????? ?????? ??????")
    void test_get_user_records() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        mockMvc.perform(get("/api/analyze/records")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1))) // ??? 6???????????????
                .andDo(document("analyze-records",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].imageId").type(JsonFieldType.NUMBER).description("????????? id"),
                                fieldWithPath("[].fileName").type(JsonFieldType.STRING).description("?????? ????????? ????????? ?????? ??????"),
                                fieldWithPath("[].uploadDate").type(JsonFieldType.STRING).description("?????? ????????? ???")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ?????? ?????????")
    void test_analyze_image() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        Long imageId = 771L;

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/analyze/{imageId}", imageId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("image-analyze",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("imageId").description("????????? id")
                        ),
                        responseFields(
                                fieldWithPath("imageId").type(JsonFieldType.NUMBER).description("????????? id"),
                                fieldWithPath("analyzeSuccess").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ????????? ?????? ?????????")
                        )
                ));

    }

    @Test
    @WithMockUser
    @DisplayName("????????? ?????? ?????? ?????? ?????????")
    void test_get_image_report() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        Long imageId = 816L;

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/analyze/result/{imageId}", imageId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("decision").value(true))
                .andDo(document("report-response",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("imageId").description("????????? id")
                        ),
                        responseFields(
                                fieldWithPath("reportId").type(JsonFieldType.NUMBER).description("report id"),
                                fieldWithPath("decision").type(JsonFieldType.BOOLEAN).description("Decision"),
                                fieldWithPath("scopeIoStart").type(JsonFieldType.NUMBER).description("SCOPE IO ?????? ???"),
                                fieldWithPath("scopeIoEnd").type(JsonFieldType.NUMBER).description("SCOPE IO ??? ???"),
                                fieldWithPath("cutOffStart").type(JsonFieldType.NUMBER).description("Cut Off ?????? ???"),
                                fieldWithPath("cutOffEnd").type(JsonFieldType.NUMBER).description("SCOPE IO ??? ???"),
                                fieldWithPath("reportDate").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("gridResults[].i_min").type(JsonFieldType.NUMBER).description("Intratumoral TIL min"),
                                fieldWithPath("gridResults[].i_avg").type(JsonFieldType.NUMBER).description("Intratumoral TIL avg"),
                                fieldWithPath("gridResults[].i_max").type(JsonFieldType.NUMBER).description("Intratumoral TIL max"),
                                fieldWithPath("gridResults[].s_min").type(JsonFieldType.NUMBER).description("Stromal TIL min"),
                                fieldWithPath("gridResults[].s_avg").type(JsonFieldType.NUMBER).description("Stromal TIL avg"),
                                fieldWithPath("gridResults[].s_max").type(JsonFieldType.NUMBER).description("Stromal TIL max")
                        )
                ));

    }
}