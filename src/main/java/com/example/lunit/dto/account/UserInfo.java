package com.example.lunit.dto.account;

import com.example.lunit.model.Account;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private String email;

    public static UserInfo of(Account account) {
        return new UserInfo(account.getEmail());
    }
}
