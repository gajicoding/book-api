package com.example.book_api.domain.log.controller;

import com.example.book_api.domain.log.dto.LogResponseDto;
import com.example.book_api.domain.log.service.LogService;
import com.example.book_api.global.dto.ApiResponse;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class LogController {

    private final LogService logService;

    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<Page<LogResponseDto>>> getLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String startAt,
            @RequestParam(required = false) String endAt,
            Pageable pageable
    ) {

        Page<LogResponseDto> logs = logService.getLogs(userId, targetType, startAt, endAt, pageable);

        return ApiResponse.success(
                HttpStatus.OK, "로그 조회가 완료되었습니다.", logs);
    }
}
