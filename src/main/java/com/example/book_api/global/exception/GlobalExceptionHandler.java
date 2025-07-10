package com.example.book_api.global.exception;

import com.example.book_api.domain.book.exception.BookException;
import com.example.book_api.domain.comment.exception.CommentException;
import com.example.book_api.domain.user.exception.UserException;
import com.example.book_api.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* 도메인별 예외 */

    // Book
    @ExceptionHandler(BookException.class)
    public ResponseEntity<ApiResponse<Void>> handleBookException(BookException e) {
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }

    // User
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserException(UserException ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Comment
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentException(CommentException ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
