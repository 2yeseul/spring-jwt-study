package com.example.lunit.dto.images;

import com.example.lunit.model.Images;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadedImage {
    private Long imageId;
    private String imagePath;
    private String fileName;

    public static UploadedImage fromImagesEntity(Images images) {
        return UploadedImage.builder()
                .imageId(images.getId())
                .imagePath(images.getImagePath())
                .fileName(parsePathToName(images.getImagePath()))
                .build();
    }

    private static String parsePathToName(String fileName) {
        fileName = fileName.substring(fileName.indexOf("_") + 1);
        return fileName;
    }
}
