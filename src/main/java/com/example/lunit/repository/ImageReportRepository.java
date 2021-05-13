package com.example.lunit.repository;

import com.example.lunit.model.Account;
import com.example.lunit.model.ImageReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageReportRepository extends JpaRepository<ImageReport, Long> {
    ImageReport findByImages_Id(Long id);

    boolean existsByImages_Account(Account account);

    boolean existsByImages_Id(Long id);

    List<ImageReport> findAllByAccount(Account account);

    boolean existsByAccount(Account account);
}
