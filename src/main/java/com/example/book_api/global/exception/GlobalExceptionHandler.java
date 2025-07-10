package com.example.book_api.global.exception;

import com.example.book_api.domain.book.exception.BookException;
import com.example.book_api.domain.comment.exception.CommentException;
import com.example.book_api.domain.user.exception.UserException;
import com.example.book_api.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookException.class)
    public ResponseEntity<ApiResponse<Void>> handleBookException(BookException e) {
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }


    // 예상치 못한 전체 예외 (최후의 보루)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException() {
        String message = "서버 내부 오류가 발생했습니다.";
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    //
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<Void>> UserException(UserException ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (exsiting, replacement) -> exsiting
                ));

        ApiResponse<Object> response = new ApiResponse<>("입력값이 올바르지 않습니다", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
  
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ApiResponse<Void>> CommentException(CommentException ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());

    }
}
