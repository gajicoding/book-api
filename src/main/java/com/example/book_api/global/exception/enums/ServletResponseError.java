package com.example.book_api.global.exception.enums;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public enum ServletResponseError {
    // JWT 관련 오류
    INVALID_JWT_SIGNATURE(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다."),
    INVALID_JWT(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다."),

    // Security 관련 오류
    UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다."),
    ACCESS_DENIED(HttpServletResponse.SC_FORBIDDEN, "접근이 거부되었습니다."),

    // Redis 관련 오류
    REDIS_CONNECTION_FAILED(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Redis 서버에 연결할 수 없습니다."),

    DATA_ACCESS_FAILED(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "데이터 접근 오류가 발생했습니다."),

    // 내부 서버 오류
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "내부 서버 오류입니다.");

    private final int httpStatus;
    private final String message;

    ServletResponseError(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
