package com.example.lunit.dto.imageReport;

import com.example.lunit.model.GridImageReport;
import com.example.lunit.model.ImageReport;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ReportResponse {
    private Long reportId;
    private boolean decision;
    private float scopeIoStart;
    private float scopeIoEnd;
    private float cutOffStart;
    private float cutOffEnd;

    private List<GridReportResponse> gridResults;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

    public static ReportResponse fromImageReports(ImageReport imageReport) {
        List<GridImageReport> gridImageReports = imageReport.getGridImageReports();

        List<GridReportResponse> gridReportResponses
                = gridImageReports.stream()
                .map(GridReportResponse::fromGridImageEntity)
                .collect(Collectors.toList());

        return ReportResponse.builder()
                .reportId(imageReport.getId())
                .decision(imageReport.isDecision())
                .scopeIoStart(imageReport.getScopeIoStart())
                .scopeIoEnd(imageReport.getScopeIoEnd())
                .cutOffStart(imageReport.getCutOffStart())
                .cutOffEnd(imageReport.getCutOffEnd())
                .reportDate(imageReport.getReportDate())
                .gridResults(gridReportResponses)
                .build();
    }
}
