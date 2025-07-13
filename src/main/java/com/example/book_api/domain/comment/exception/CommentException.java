package com.example.book_api.domain.comment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommentException extends RuntimeException {
    private final HttpStatus status;
    public CommentException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
