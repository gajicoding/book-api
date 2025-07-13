package com.example.book_api.global.util;

import com.example.book_api.domain.log.enums.RequestMethod;
import com.example.book_api.domain.log.enums.TargetType;
import com.example.book_api.global.util.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;


/**
 * 로깅 요소들을 추출하는 유틸리티 클래스
 *
 */
@Slf4j
@UtilityClass
public class LogInfoExtractor {

    private static final List<String> ID_FIELD_NAMES = List.of("id", "commentId", "bookId", "userId", "ratingId");

    public HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull (RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public Long extractUserId(HttpServletRequest request, JwtUtil jwtUtil) {
        try {
            String bearerToken = request.getHeader("Authorization");

            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                return Long.parseLong(jwtUtil.extractClaims(token).getSubject());
            }
        } catch (Exception e) {
            log.warn("JWT 토큰에서 사용자 ID를 추출하는데 실패했습니다.", e);
        }

        // 비로그인 사용자 또는 토큰 오류 시
        return null;
    }


    public Long extractTargetId(ResponseEntity<?> responseEntity, HttpServletRequest request) {
        String method = request.getMethod();

        if ("POST".equalsIgnoreCase(method)) {
            return extractIdFromResponseBody(responseEntity.getBody());
        }

        if ("PATCH".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            return extractIdFromUri(request);
        }

        return null;
    }

    private Long extractIdFromResponseBody(Object responseBody) {
        if (responseBody == null) {
            return null;
        }

        try {
            // 1. responseBody (ApiResponse)에서 'data' 필드를 찾음
            Field dataField = responseBody.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            Object dataObject = dataField.get(responseBody); // 실제 DTO (BookResponseDto)

            if (dataObject == null) return null;

            // 2. dataObject (BookResponseDto)에서 ID 필드를 찾음
            for (String fieldName : ID_FIELD_NAMES) {
                try {
                    Field idField = dataObject.getClass().getDeclaredField(fieldName);
                    idField.setAccessible(true);
                    Object value = idField.get(dataObject);

                    if (value instanceof Long l) return l;

                } catch (NoSuchFieldException ignored) {
                    // 해당 필드가 없으면 다음 필드명으로 계속
                }
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Failed to extract ID from response body via reflection.", e);
        }

        return null;
    }

    private Long extractIdFromUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] segments = uri.split("/");

        for (int i = segments.length - 1; i >= 0; i--) {
            try {
                return Long.parseLong(segments[i]);
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    public int extractStatusCode(Object result) {

        if (result instanceof ResponseEntity<?> responseEntity) {
            int status = responseEntity.getStatusCode().value();
            return status;

        }

        return 500;
    }

    public RequestMethod extractRequestMethod(HttpServletRequest request) {
        try {
            return RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * TargetType을 추출하는 기능
     *
     * @param uri 요청 uri 주소
     * @return TargetType
     */
    public TargetType extractTargetType(String uri) {

        if (uri.matches(".*/comments.*")) return TargetType.COMMENT;
        if (uri.matches(".*/ratings.*")) return TargetType.RATING;
        if (uri.matches(".*/books(/.*)?$")) return TargetType.BOOK;
        if (uri.matches(".*/users.*")) return TargetType.USER;
        if (uri.matches(".*/auth.*")) return TargetType.USER;

        return null;
    }

    public static String extractMessageFromResponseBody(Object responseBody) {
        if (responseBody == null) return null;
        try {
            Field messageField = responseBody.getClass().getDeclaredField("message");
            messageField.setAccessible(true);
            Object messageObj = messageField.get(responseBody);
            if (messageObj instanceof String msg) return msg;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("Failed to extract message from response body", e);
        }
        return null;
    }
}
