package com.example.lunit.dto.imageReport;

import com.example.lunit.model.ImageReport;
import com.example.lunit.model.Images;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RequestRecords {
    private Long imageId;
    private String fileName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate uploadDate;


    public static RequestRecords fromImageEntity(Images images) {
        return RequestRecords.builder()
                .imageId(images.getId())
                .fileName(parsePathToName(images.getImagePath()))
                .uploadDate(images.getUploadDate())
                .build();
    }

    public static RequestRecords fromImageReport(ImageReport imageReport) {
        Images images = imageReport.getImages();
        return RequestRecords.fromImageEntity(images);
    }

    private static String parsePathToName(String fileName) {
        fileName = fileName.substring(fileName.indexOf("_") + 1);
        return fileName;
    }
}
