package com.example.book_api.domain.book.exception;

import org.springframework.http.HttpStatus;

public class InvalidSearchConditionException extends BookException {
    public InvalidSearchConditionException(HttpStatus status, String message) {
        super(status, message);
    }
}
