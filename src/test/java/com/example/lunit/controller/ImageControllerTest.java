package com.example.lunit.controller;

import com.example.lunit.dto.account.LoginRequest;
import com.example.lunit.dto.images.DownloadRequest;
import com.example.lunit.dto.images.SearchImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONValue;
import org.aspectj.lang.annotation.Before;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("이미지 url 얻기")
    void test_get_url_by_id() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        Long imageId = 817L;

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/image/url/{imageId}", imageId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //.andExpect(jsonPath("$.imagePath").value("https://seul-lunit-test.s3.ap-northeast-2.amazonaws.com/static/bbf98cfaab33492e877f5b220a9f6121_IMG_7005_SURFACETENSION_2048x.jpeg"))
                .andDo(document("image-url",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("imageId").description("이미지 id")
                        ),
                        responseFields(
                                fieldWithPath("imagePath").type(JsonFieldType.STRING).description("이미지의 주소값")
                        )

                ));
    }

    @Test
    @DisplayName("이미지 다운로드")
    @WithMockUser
    void test_download_from_url() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        Long imageId = 816L;

        DownloadRequest request = new DownloadRequest();
        request.setPath("/Users/2yeseul/Downloads");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/image/download/{imageId}", imageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("image-download",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("imageId").description("이미지 id")
                        ),
                        requestFields(
                                fieldWithPath("path").type(JsonFieldType.STRING).description("저장할 경로")
                        )

                ));
    }

    @Test
    @DisplayName("파일명 검색을 통한 이미지 리스트 출력 테스트")
    @WithMockUser
    void test_search_images() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        String fileName = "lion";

        SearchImage searchImage = new SearchImage();
        searchImage.setFileName(fileName);

        mockMvc.perform(post("/api/image/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchImage)))
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andDo(print())
                .andDo(document("image-search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일 이름")
                        ),
                        responseFields(
                                fieldWithPath("[].imageId").type(JsonFieldType.NUMBER).description("image id"),
                                fieldWithPath("[].imagePath").type(JsonFieldType.STRING).description("image 주소"),
                                fieldWithPath("[].fileName").type(JsonFieldType.STRING).description("file name")
                        )

                ));

    }

    @Test
    @DisplayName("사용자가 업로드한 파일 전체 조회 테스트")
    @WithMockUser
    void test_get_all_images() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sssskkkk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        int page = 0;

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/image/list/{page}", page)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.length()", equalTo(4)))
                .andDo(document("image-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("page").description("page - 0부터 시작")
                        ),
                        responseFields(
                                fieldWithPath("[].imageId").type(JsonFieldType.NUMBER).description("image id"),
                                fieldWithPath("[].imagePath").type(JsonFieldType.STRING).description("image url"),
                                fieldWithPath("[].fileName").type(JsonFieldType.STRING).description("파일명")
                        )
                ));
    }


    @Test
    @DisplayName("이미지 업로드 테스트")
    @WithMockUser
    void test_upload_img() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test1@email.com");
        loginRequest.setPassword("sksksksk");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        String path = "/Users/2yeseul/IdeaProjects/lunit/src/main/resources/static/IMG_5550.jpg";
        MockMultipartFile files = new MockMultipartFile("files", "IMG_5550", null, new FileInputStream(new File(path)));

        mockMvc.perform(multipart("/api/image/upload")
                .file(files))
                .andExpect(status().isOk())
                .andDo(document("upload-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

    }


}