package com.example.lunit.dto.account;

import com.example.lunit.model.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserProfile {
    private Long userId;
    private String email;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate signUpDate;

    public static UserProfile of(Account account) {
        return UserProfile.builder()
                .userId(account.getId())
                .email(account.getEmail())
                .name(account.getName())
                .signUpDate(account.getSignUpDate())
                .build();
    }
}
