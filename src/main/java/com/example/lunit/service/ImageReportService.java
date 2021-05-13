package com.example.lunit.service;

import com.example.lunit.dto.imageReport.AnalyzeResponse;
import com.example.lunit.dto.imageReport.GridReportResponse;
import com.example.lunit.dto.imageReport.RequestRecords;
import com.example.lunit.model.Account;
import com.example.lunit.model.GridImageReport;
import com.example.lunit.model.ImageReport;
import com.example.lunit.model.Images;
import com.example.lunit.repository.GridImageReportRepository;
import com.example.lunit.repository.ImageReportRepository;
import com.example.lunit.dto.imageReport.ReportResponse;
import com.example.lunit.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageReportService {
    private final ImageReportRepository imageReportRepository;
    private final ImageRepository imageRepository;
    private final GridImageReportRepository gridReportRepository;

    Random random = new Random();

    // 모든 분석 결과 조회
    public List<ReportResponse> getAllImageReports(Account account) {
        if(imageReportRepository.existsByImages_Account(account)) {
            List<ImageReport> imageReports = imageReportRepository.findAllByAccount(account);

            return imageReports.stream()
                    .map(ReportResponse::fromImageReports)
                    .collect(Collectors.toList());
        }

        else return null;
    }

    // 사용자의 분석 요청 이력 조회
    public List<RequestRecords> getUserRequestRecords(Account account) {
        if(imageReportRepository.existsByAccount(account)) {
            List<ImageReport> imageReports = imageReportRepository.findAllByAccount(account);

            return imageReports.stream()
                    .map(RequestRecords::fromImageReport)
                    .collect(Collectors.toList());
        }

        else return null;
    }


    // 분석 결과 response
    public ReportResponse imageReportProcess(Account account, Long imageId) throws Exception{
        if(imageRepository.existsImagesByAccountAndId(account, imageId)) {
            ImageReport imageReport = imageReportRepository.findByImages_Id(imageId);

            return ReportResponse.fromImageReports(imageReport);
        }

        else return null;
    }


    // 이미지 분석 요청
    @Transactional
    public AnalyzeResponse imageAnalyzeProcess(Account account, Long imageId) throws InterruptedException {
        if(imageRepository.existsImagesByAccountAndId(account, imageId) && !imageReportRepository.existsByImages_Id(imageId)) {
            // 1초 * 1분 * 10분
            // 분석에 10분 이상 소요되는 점 감안하여 설정
            Thread.sleep(1000 * 60 * 10);
            Long imageReportId = makeDummyReport(account, imageId).getId();
            makeDummyGridValues(imageReportId);

            return AnalyzeResponse.successResponse(imageId);
        }
        else
            if(imageReportRepository.existsByImages_Id(imageId)) {
                return AnalyzeResponse.isAlreadyExists(imageId);
            }
            return AnalyzeResponse.errorOccurs(imageId);

    }

    // >>>> >>>> >>>> >>>> >>>> fake analyze

    private ImageReport makeDummyReport(Account account, Long imageId) {
        boolean decision = random.nextBoolean();

        Images images = imageRepository.findById(imageId).get();

        float min = random.nextFloat();
        float max = random.nextFloat();

        if(min > max) {
            float temp = max;
            max = min;
            min = temp;
        }

        ImageReport imageReport = ImageReport.builder()
                .decision(decision)
                .scopeIoStart(min)
                .scopeIoEnd(max)
                .cutOffStart(min)
                .cutOffEnd(max)
                .images(images)
                .account(account)
                .reportDate(LocalDate.now())
                .build();

        return imageReportRepository.save(imageReport);
    }


    private void makeDummyGridValues(Long imageReportId) {
        List<GridImageReport> gridImageReports = new ArrayList<>();
        for(int i=0; i<30; i++) {
            float min = random.nextFloat();
            float max = random.nextFloat();

            if(min > max) {
                float temp = max;
                max = min;
                min = temp;
            }

            ImageReport imageReport = imageReportRepository.findById(imageReportId).get();

            GridImageReport gridReport = GridImageReport.builder()
                    .i_min(min)
                    .i_avg((max + min) / 2)
                    .i_max(max)
                    .s_min(min)
                    .s_avg((max + min) / 2)
                    .s_max(max)
                    .imageReport(imageReport)
                    .images(imageReport.getImages())
                    .build();

            gridImageReports.add(gridReport);
        }

        gridReportRepository.saveAll(gridImageReports);
    }
}
