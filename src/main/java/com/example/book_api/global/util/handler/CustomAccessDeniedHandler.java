package com.example.book_api.global.util.handler;

import com.example.book_api.global.exception.enums.ServletResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(ServletResponseError.ACCESS_DENIED.getHttpStatus());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", ServletResponseError.ACCESS_DENIED.getHttpStatus());
        body.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        body.put("message", ServletResponseError.ACCESS_DENIED.getMessage());
        body.put("path", request.getRequestURI());

        String jsonBody = objectMapper.writeValueAsString(body);

        response.getWriter().write(jsonBody);
    }
}
