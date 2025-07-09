package com.example.book_api.domain.user.enums;

import com.example.book_api.domain.auth.exception.InvalidRequestException;

import java.util.Arrays;

public enum Role {
    ADMIN, USER;

    public static Role of(String role) {
        return Arrays.stream(Role.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 권한"));
    }
}
