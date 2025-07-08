package com.example.book_api.domain.book.exception;


import org.springframework.http.HttpStatus;

public class NotFoundException extends BookException {
    public NotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
