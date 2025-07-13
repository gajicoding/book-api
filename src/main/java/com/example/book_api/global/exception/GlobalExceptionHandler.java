package com.example.book_api.global.exception;

import com.example.book_api.domain.book.exception.BookException;
import com.example.book_api.domain.comment.exception.CommentException;
import com.example.book_api.domain.user.exception.UserException;
import com.example.book_api.global.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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


    /* Global 예외 */

    // Redis 연결 실패 시
    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleRedisConnectionFailure(RedisConnectionFailureException ex) {
        log.error("Redis 연결 실패: {}", ex.getMessage(), ex);
        return ApiResponse.error(HttpStatus.SERVICE_UNAVAILABLE, "Redis 서버에 연결할 수 없습니다.");
    }

    // 데이터 접근 실패
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataAccessException(DataAccessException ex) {
        log.error("데이터 접근 실패: {}", ex.getMessage(), ex);
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 접근 오류가 발생했습니다.");
    }

    // vaildation 오류
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

    // 예상치 못한 전체 예외 (최후의 보루)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleGenericException() {
//        String message = "서버 내부 오류가 발생했습니다.";
//        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, message);
//    }
}
