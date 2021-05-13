package com.example.lunit.repository;

import com.example.lunit.model.Account;
import com.example.lunit.model.Images;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Images, Long> {
    boolean existsImagesByAccountAndId(Account account, Long id);
    boolean existsByAccount(Account account);

    List<Images> findAllByAccount(Account account);

    Page<Images> findAllByAccount(Account account, Pageable pageable);

    List<Images> findAllByImagePathContainingAndAccount(String imagePath, Account account);
    boolean existsByImagePathContainingAndAccount(String imagePath, Account account);

    boolean existsByAccountAndId(Account account, Long id);

    Images findByAccountAndId(Account account, Long id);

}
