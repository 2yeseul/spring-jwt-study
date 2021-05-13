package com.example.lunit.dto.images;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UploadResponse {
    private Long uploaderId;
    private List<String> fileNames;

    public static UploadResponse makeResponse(Long uploadedId, List<String> fileNames) {
        return UploadResponse.builder()
                .uploaderId(uploadedId)
                .fileNames(fileNames)
                .build();
    }
}
