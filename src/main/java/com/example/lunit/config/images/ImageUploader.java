package com.example.lunit.config.images;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


public interface ImageUploader {
    String upload(MultipartFile multipartFile, String dirName) throws Exception;
    void delete(String fileName);
}
