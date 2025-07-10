package com.example.book_api.domain.log.dto;

import com.example.book_api.domain.log.enums.ActivityType;
import com.example.book_api.domain.log.enums.RequestMethod;
import com.example.book_api.domain.log.enums.TargetType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogResponseDto {
    private Long id;

    private Long userId;

    private TargetType targetType;

    private Long targetId;

    private RequestMethod requestMethod;

    private String requestUri;

    private ActivityType activityType;

    private int statusCode;

    private String message;

    private LocalDateTime createdAt;


    public LogResponseDto(Long id, Long userId, TargetType targetType, Long targetId,
                          RequestMethod requestMethod, String requestUri, ActivityType activityType,
                          int statusCode, String message, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.requestMethod = requestMethod;
        this.requestUri = requestUri;
        this.activityType = activityType;
        this.statusCode = statusCode;
        this.message = message;
        this.createdAt = createdAt;
    }
}
