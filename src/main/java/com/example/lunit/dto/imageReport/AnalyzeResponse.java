package com.example.lunit.dto.imageReport;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AnalyzeResponse {
    private Long imageId;
    private boolean analyzeSuccess;
    private String message;

    public static AnalyzeResponse isAlreadyExists(Long imageId) {
        return AnalyzeResponse.builder()
                .imageId(imageId)
                .analyzeSuccess(false)
                .message("the result of analyzing is already exists")
                .build();
    }

    public static AnalyzeResponse errorOccurs(Long imageId) {
        return AnalyzeResponse.builder()
                .imageId(imageId)
                .analyzeSuccess(false)
                .message("error occurs during analyzing the image")
                .build();
    }

    public static AnalyzeResponse successResponse(Long imageId) {
        return AnalyzeResponse.builder()
                .imageId(imageId)
                .analyzeSuccess(true)
                .message("true")
                .build();
    }
}
