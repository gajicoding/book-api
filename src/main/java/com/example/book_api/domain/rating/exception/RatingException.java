package com.example.book_api.domain.rating.exception;

import com.example.book_api.domain.rating.exception.RatingErrorCode;
import lombok.Getter;

@Getter
public class RatingException extends RuntimeException {
    private final RatingErrorCode errorCode;

    public RatingException(RatingErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

