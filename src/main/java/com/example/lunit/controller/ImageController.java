package com.example.lunit.controller;

import com.example.lunit.dto.images.*;
import com.example.lunit.model.Account;
import com.example.lunit.model.CurrentUser;
import com.example.lunit.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;

    // 이미지 id를 통하여 image url 반환
    @GetMapping("/url/{imageId}")
    public ImgUrl getImgUrl(@PathVariable Long imageId) {
        return imageService.getImagesUrlByImageId(imageId);
    }

    // 이미지 삭제
    @PostMapping("/delete/{imageId}")
    public void deleteImage(@CurrentUser Account account, @PathVariable Long imageId) {
        imageService.deleteImage(account, imageId);
    }

    // 이미지 업로드
    @PostMapping("/upload")
    public UploadResponse uploadImage(@CurrentUser Account account, @ModelAttribute UploadRequest request) throws Exception {
        return imageService.uploadSlideImages(account, request);
    }

    // 이미지 다운로드
    // TODO : api 실행 시 시간이 조금 많이 소요되는데 해결할 방법 생각해보기
    @PostMapping("/download/{imageId}")
    public void downloadImage(@PathVariable Long imageId, @RequestBody DownloadRequest request) throws Exception {
        imageService.downloadImageProcess(imageId, request);
    }

    // 사용자가 업로드한 이미지 리스트 조회
    @GetMapping("/list/{page}")
    public List<UploadedImage> getUserImageList(@CurrentUser Account account, @PathVariable int page) {
        return imageService.getUserImageList(account, page);
    }

    // 파일명을 통한 이미지 검색
    @PostMapping("/search")
    public List<UploadedImage> searchImagesByFileName(@CurrentUser Account account, @RequestBody SearchImage searchImage) {
        return imageService.searchImageProcess(account, searchImage);
    }

    // 개별 이미지 정보 조회
    @GetMapping("/info/{imageId}")
    public ImageInfo getImageInfo(@CurrentUser Account account, @PathVariable Long imageId) {
        return imageService.getImageInfo(account, imageId);
    }
}
