package com.example.lunit.model;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String roleUser;

    Role(String roleUser) {
        this.roleUser = roleUser;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return roleUser;
    }
}
