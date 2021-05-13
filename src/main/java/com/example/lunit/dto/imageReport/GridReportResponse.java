package com.example.lunit.dto.imageReport;

import com.example.lunit.model.GridImageReport;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GridReportResponse {
    // i_** : Intratumoral TIL destiny
    private float i_min;
    private float i_avg;
    private float i_max;

    // s_** : Stromal TIL destiny
    private float s_min;
    private float s_avg;
    private float s_max;

    public static GridReportResponse fromGridImageEntity(GridImageReport gridImageReport) {
        return GridReportResponse.builder()
                .i_min(gridImageReport.getI_min())
                .i_avg(gridImageReport.getI_avg())
                .i_max(gridImageReport.getI_max())
                .s_min(gridImageReport.getI_min())
                .s_avg(gridImageReport.getS_avg())
                .s_max(gridImageReport.getS_max())
                .build();
    }
}
