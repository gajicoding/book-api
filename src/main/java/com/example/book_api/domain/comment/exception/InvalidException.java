package com.example.book_api.domain.comment.exception;

import org.springframework.http.HttpStatus;

public class InvalidException extends CommentException {
    public InvalidException(HttpStatus status, String message) {
        super(status, message);
    }
}
