package com.example.book_api.global.exception;

import com.example.book_api.domain.auth.exception.InvalidRequestException;
import com.example.book_api.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 예상치 못한 전체 예외 (최후의 보루)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException() {
        String message = "서버 내부 오류가 발생했습니다.";
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    //
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<Void>> invalidRequestException(InvalidRequestException ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
