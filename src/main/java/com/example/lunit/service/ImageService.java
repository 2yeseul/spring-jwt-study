package com.example.lunit.service;

import com.example.lunit.dto.images.*;
import com.example.lunit.model.Account;
import com.example.lunit.model.Images;
import com.example.lunit.repository.ImageRepository;
import com.example.lunit.config.images.ImageUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageUploader uploader;

    // image id를 통해 image path 값 얻기
    public ImgUrl getImagesUrlByImageId(Long imageId) {
        Images images = imageRepository.findById(imageId).get();
        return ImgUrl.builder()
                .imagePath(images.getImagePath())
                .build();
    }

    // 이미지 다운로드
    public void downloadImageProcess(Long imageId, DownloadRequest request) throws Exception {
        Images images = imageRepository.findById(imageId).get();
        String imageUrl = images.getImagePath();

        downloadImage(imageUrl, request.getPath());
    }

    private void downloadImage(String imageUrl, String savePath) throws Exception {
        URL url;
        InputStream inputStream;
        OutputStream outputStream;

        try {
            url = new URL(imageUrl);
            inputStream = url.openStream();
            outputStream = new FileOutputStream(savePath + "/" + LocalDateTime.now().toString() + ".jpg");
            while (true) {
                // read image
                int imageData = inputStream.read();
                if(imageData == -1) break;

                // write image
                outputStream.write(imageData);
                log.info(">>>>> url : " + imageUrl + "다운로드 완료");
            }

            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // S3 img 삭제
    @Transactional
    public void deleteImage(Account account, Long imageId) {
        Images images = imageRepository.findById(imageId).get();
        String filePath = images.getImagePath();
        String fileName = "static/" + filePath.substring(filePath.lastIndexOf("/") + 1);
        if(imageRepository.existsByAccountAndId(account, imageId)) {
            // aws s3 삭제
            uploader.delete(fileName);
        }
    }

    // 이미지에 대한 정보
    public ImageInfo getImageInfo(Account account, Long imageId) {
        Images images = imageRepository.findById(imageId).get();

        return ImageInfo.builder()
                .imageId(imageId)
                .fileName(parsePathToName(images.getImagePath()))
                .filePath(images.getImagePath())
                .build();
    }

    // 이미지 검색
    public List<UploadedImage> searchImageProcess(Account account, SearchImage searchImage) {
        String fileName = searchImage.getFileName();
        fileName = parsePathToName(fileName);

        List<UploadedImage> uploadedImages = new ArrayList<>();

        if(imageRepository.existsByImagePathContainingAndAccount(fileName, account)) {
            List<Images> images = imageRepository.findAllByImagePathContainingAndAccount(fileName, account);
            uploadedImages = convertToUploadedImageList(images);
        }

        return uploadedImages;
    }

    private String parsePathToName(String fileName) {
        fileName = fileName.substring(fileName.indexOf("_") + 1);
        return fileName;
    }

    // 유저가 올린 전체 파일리스트 확인
    public List<UploadedImage> getUserImageList(Account account, int page) {
        List<UploadedImage> uploadedImages;
        List<Images> imagesList = new ArrayList<>();
        if(imageRepository.existsByAccount(account)) {
            Page<Images> imagesPage = imageRepository.findAllByAccount(account, PageRequest.of(page, 10, Sort.by("id")));
            imagesList = imagesPage.getContent();
        }

        uploadedImages = convertToUploadedImageList(imagesList);
        return uploadedImages;
    }

    private List<UploadedImage> convertToUploadedImageList(List<Images> images) {
        return images.stream()
                .map(UploadedImage::fromImagesEntity)
                .collect(Collectors.toList());

    }

    // 이미지 업로드
    @Transactional
    public UploadResponse uploadSlideImages(Account account, UploadRequest request) throws Exception {
        List<String> imgPaths = new ArrayList<>();
        List<MultipartFile> fileList = request.getFiles();

        imageSaveProcess(account, fileList, imgPaths);

        return UploadResponse.makeResponse(account.getId(), imgPaths);
    }

    private void imageSaveProcess(Account account, List<MultipartFile> fileList, List<String> imgPaths) throws Exception {
        for (MultipartFile file : fileList) {
            String path = uploader.upload(file, "static");

            Images images = Images.builder()
                    .account(account)
                    .imagePath(path)
                    .uploadDate(LocalDate.now())
                    .build();

            imageRepository.save(images);
            imgPaths.add(path);
        }
    }
}
