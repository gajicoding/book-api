package com.example.book_api.domain.comment.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CommentException {
    public NotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
