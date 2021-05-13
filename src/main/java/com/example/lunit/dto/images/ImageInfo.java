package com.example.lunit.dto.images;

import com.example.lunit.model.Account;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageInfo {
    private Long imageId;
    private String fileName;
    private String filePath;

}
