package com.example.lunit.dto.account;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SignUpForm {
    private String email;
    private String password;
    private String name;
    private String address;
    private String telephone;
}
