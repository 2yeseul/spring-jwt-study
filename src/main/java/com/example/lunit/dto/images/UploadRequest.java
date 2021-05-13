package com.example.lunit.dto.images;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadRequest {
    List<MultipartFile> files;
}
