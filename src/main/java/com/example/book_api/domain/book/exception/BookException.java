package com.example.book_api.domain.book.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BookException extends RuntimeException {
    private final HttpStatus status;
    public BookException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
