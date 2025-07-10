package com.example.book_api.global.aop;

import com.example.book_api.domain.log.entity.Log;
import com.example.book_api.domain.log.enums.ActivityType;
import com.example.book_api.domain.log.enums.RequestMethod;
import com.example.book_api.domain.log.enums.TargetType;
import com.example.book_api.domain.log.repository.LogRepository;
import com.example.book_api.domain.log.service.LogService;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.repository.UserRepository;
import com.example.book_api.global.annotation.Logging;
import com.example.book_api.global.config.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.weaver.Lint;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final LogService logService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Around("@annotation(logging)")
    public Object logActivity(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 메서드 실행 이전 초기화 설정
        int statusCode = 500;
        String message = "알 수 없는 오류";
        Long targetId = null;
        Object result;

        try {
            result = joinPoint.proceed();

            statusCode = extractStatusCode(result);
            targetId = extractTargetId(result, joinPoint, request);
            message = "성공";

            return result;

        } catch (Exception e) {
            log.error("로그 등록 실패", e);

            statusCode = 500;
            targetId = extractTargetId(null, joinPoint, request);
            message = "실패: " + e.getMessage();

            throw e;

        } finally {
            Log logDate = buildLog(logging, request, statusCode, message, targetId);
            logService.saveLog(logDate);
        }
    }

    private Log buildLog(Logging logging, HttpServletRequest request,
                         int statusCode, String message, Long targetId) {
        Long userId = extractUserId(request);
        String uri = request.getRequestURI();
        TargetType targetType = extractTargetType(uri);
        RequestMethod requestMethod = extractRequestMethod(request);
        ActivityType activityType = logging.activityType();

        return Log.builder()
                .userId(userId)
                .targetType(targetType)
                .targetId(targetId)
                .requestMethod(requestMethod)
                .requestUri(uri)
                .activityType(activityType)
                .statusCode(statusCode)
                .message(message)
                .build();
    }

    private Long extractUserId(HttpServletRequest request) {
//        jwt util을 활용하여 userId 추출
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return null;
        }

        String token = bearerToken.substring(7);

        Claims claims = jwtUtil.extractClaims(token);

        return claims.get("userId", Long.class);
    }

    private Long extractTargetId(Object result, ProceedingJoinPoint joinPoint, HttpServletRequest request) {
        String method = request.getMethod();

        if ("POST".equalsIgnoreCase(method)) {
            return extractIdFromResult(result);
        }

        if ("PATCH".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            return extractIdFromArgs(joinPoint.getArgs());
        }

        return null;
    }

    private Long extractIdFromResult(Object result) {
        if(result == null) return null;

        try {
            for (String fieldName : List.of("id", "commentId", "bookId", "userId")) {
                Field field = result.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(result);

                if (value instanceof Long l) return l;
            }
        } catch (Exception ignored) {
            // 필드가 없다면 다음 필드 검색
        }
        return null;
    }

    private Long extractIdFromArgs(Object[] args) {
        if (args == null) return null;

        for (Object arg : args) {
            if (arg instanceof Long l) return l;

            for (String fieldName : List.of("id", "commentId", "bookId", "userId")) {
                try {
                    Field field = arg.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(arg);

                    if(value instanceof Long l) return l;
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                    // 필드가 없다면 다음 필드 검색
                }
            }
        }
        return null;
    }

    private int extractStatusCode(Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            return responseEntity.getStatusCode().value();
        }
        return 200;
    }

    private RequestMethod extractRequestMethod(HttpServletRequest request) {
        try {
            return RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            log.error("request method 추출 실패");
            return null;
        }
    }

    /**
     * TargetType을 추출하는 기능
     *
     * @param uri 요청 uri 주소
     * @return TargetType
     */
    private TargetType extractTargetType(String uri) {
        if (uri == null) return null;

        if (uri.matches(".*/books/\\d+/comments.*")) return TargetType.COMMENT;
        if (uri.matches(".*/books/\\d+/ratings.*")) return TargetType.RATING;
        if (uri.matches(".*/books/\\d+.*")) return TargetType.BOOK;
        if (uri.matches(".*/users.*")) return TargetType.USER;
        if (uri.matches(".*/auth.*")) return TargetType.USER;

        return null;
    }
}
