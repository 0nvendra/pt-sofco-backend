package com.example.test_pt_sofco_graha_gaji.id.model;

public enum UserRole {
    SUPER_ADMIN("SUPER ADMIN"),
    ADMIN("ADMIN"),
    MODERATOR("MODERATOR"),
    USER("USER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
