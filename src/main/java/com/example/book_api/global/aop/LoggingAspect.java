package com.example.book_api.global.aop;

import com.example.book_api.domain.log.entity.Log;
import com.example.book_api.domain.log.enums.ActivityType;
import com.example.book_api.domain.log.service.LogService;
import com.example.book_api.global.annotation.Logging;
import com.example.book_api.global.config.JwtUtil;
import com.example.book_api.global.util.LogInfoExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final LogService logService;
    private final JwtUtil jwtUtil;

    @Around("@annotation(logging) && !within(com.example.book_api.global.exception..*)")
    public Object logActivity(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {

        HttpServletRequest request = LogInfoExtractor.getCurrentRequest();
        Log.LogBuilder logBuilder = Log.builder();

        logBuilder
                .activityType(logging.activityType())
                .requestUri(request.getRequestURI())
                .requestMethod(LogInfoExtractor.extractRequestMethod(request))
                .userId(LogInfoExtractor.extractUserId(request, this.jwtUtil))
                .targetType(LogInfoExtractor.extractTargetType(request.getRequestURI()));

        try {
            Object result = joinPoint.proceed();

            if (result instanceof ResponseEntity<?> responseEntity) {

                logBuilder.statusCode(LogInfoExtractor.extractStatusCode(result));
                logBuilder.targetId(LogInfoExtractor.extractTargetId(responseEntity, request));

            } else {
                logBuilder.statusCode(200);
            }

            logBuilder.message("성공");

            logService.saveLog(logBuilder.build());
            log.info("Success Logged: {}", logBuilder.build().toString());

            return result;

        } catch (Exception e) {
            throw e;
        }
    }

    @Around("within(com.example.book_api.global.exception..*)")
    public Object logActivityFailure(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = LogInfoExtractor.getCurrentRequest();
        Log.LogBuilder logBuilder = Log.builder();

        logBuilder
                .activityType(ActivityType.EXCEPTION)
                .requestUri(request.getRequestURI())
                .requestMethod(LogInfoExtractor.extractRequestMethod(request))
                .userId(LogInfoExtractor.extractUserId(request, this.jwtUtil))
                .targetType(LogInfoExtractor.extractTargetType(request.getRequestURI()));

        Object result = joinPoint.proceed();

        if (result instanceof ResponseEntity<?> responseEntity) {
            int statusCode = responseEntity.getStatusCode().value();
            logBuilder.statusCode(statusCode);
            logBuilder.targetId(LogInfoExtractor.extractTargetId(responseEntity, request));

            String message = LogInfoExtractor.extractMessageFromResponseBody(responseEntity.getBody());
            logBuilder.message(statusCode >= 400
                    ? "실패 : " + (message != null ? message : "에러가 발생했습니다.")
                    : (message != null ? message : "성공"));
        } else {
            logBuilder.statusCode(200);
            logBuilder.message("성공");
        }

        logService.saveLog(logBuilder.build());
        log.info("Exception Handler Logged: {}", logBuilder.build().toString());

        return result;
    }
}