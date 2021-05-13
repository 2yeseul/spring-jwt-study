package com.example.lunit.controller;

import com.example.lunit.dto.imageReport.AnalyzeResponse;
import com.example.lunit.dto.imageReport.ReportResponse;
import com.example.lunit.dto.imageReport.RequestRecords;
import com.example.lunit.model.Account;
import com.example.lunit.model.CurrentUser;
import com.example.lunit.service.ImageReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analyze")
@Slf4j
public class ImageReportController {

    private final ImageReportService imageReportService;

    // 분석 이력 조회
    @GetMapping("/records")
    public List<RequestRecords> getUserRequestRecords(@CurrentUser Account account) {
        return imageReportService.getUserRequestRecords(account);
    }

    // 슬라이드 이미지 분석 요청 (dummy)
    @PostMapping("/{imageId}")
    public AnalyzeResponse analyzeImage(@CurrentUser Account account, @PathVariable Long imageId) throws InterruptedException {
        return imageReportService.imageAnalyzeProcess(account, imageId);
    }

    // 슬라이드 이미지 분석 결과 반환
    @GetMapping("/result/{imageId}")
    public ReportResponse getImageReport(@CurrentUser Account account, @PathVariable Long imageId) throws Exception {
        return imageReportService.imageReportProcess(account, imageId);
    }

    // 이미지 분석 결과 전체 조회
    @GetMapping("/result/all")
    public List<ReportResponse> getAllImageReports(@CurrentUser Account account) {
        return imageReportService.getAllImageReports(account);
    }
}
