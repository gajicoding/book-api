package com.example.book_api.domain.log.dto;

import com.example.book_api.domain.log.enums.TargetType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogRequestDto {
    private Long userId;
    private String targetType;
    private String startAt;
    private String endAt;

    public LogRequestDto(Long userId, String targetType, String startAt, String endAt) {
        this.userId = userId;
        this.targetType = targetType;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void dateConvert(String startAt, String endAt) {

    }
}
