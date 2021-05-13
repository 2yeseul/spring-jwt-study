package com.example.lunit.repository;

import com.example.lunit.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByEmail(String email);
    boolean existsByEmail(String email);
}
