package com.example.book_api.global.aop;

import com.example.book_api.domain.log.entity.Log;
import com.example.book_api.domain.log.enums.RequestMethod;
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

    @Around("@annotation(logging)")
    public Object logActivity(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {

        HttpServletRequest request = LogInfoExtractor.getCurrentRequest();
        Log.LogBuilder logBuilder = Log.builder();

        logBuilder.activityType(logging.activityType())
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
            ;
            logBuilder.message("성공");
            return result;

        } catch (Exception e) {
            log.error("로그 등록 실패", e);

            logBuilder.statusCode(500)
                    .message("실패 : " + e.getMessage());

            throw e;

        } finally {
            logService.saveLog(logBuilder.build());
            log.info("Activity Logged: {}", logBuilder.build().toString());
        }
    }
}