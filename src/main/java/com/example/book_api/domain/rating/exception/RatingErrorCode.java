package com.example.book_api.domain.rating.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RatingErrorCode {
    RATING_NOT_FOUND(HttpStatus.NOT_FOUND, "평점을 찾을 수 없습니다."),
    DUPLICATE_RATING(HttpStatus.CONFLICT, "이미 평점을 등록한 책입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
