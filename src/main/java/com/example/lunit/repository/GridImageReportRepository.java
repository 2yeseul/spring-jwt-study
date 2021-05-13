package com.example.lunit.repository;

import com.example.lunit.model.Account;
import com.example.lunit.model.GridImageReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GridImageReportRepository extends JpaRepository<GridImageReport, Long> {
    List<GridImageReport> findAllByImages_Id(Long imageId);
    boolean existsByImages_Id(Long imageId);
}
