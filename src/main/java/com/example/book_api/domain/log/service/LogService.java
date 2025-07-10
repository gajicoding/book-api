package com.example.book_api.domain.log.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.log.dto.LogRequestDto;
import com.example.book_api.domain.log.dto.LogResponseDto;
import com.example.book_api.domain.log.entity.Log;
import com.example.book_api.domain.log.enums.ActivityType;
import com.example.book_api.domain.log.enums.RequestMethod;
import com.example.book_api.domain.log.enums.TargetType;
import com.example.book_api.domain.log.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(Log log){
        logRepository.save(log);
    }

    public Page<LogResponseDto> getLogs(Long userId, String targetType, String startAt, String endAt,
                                        Pageable pageable) {
        LogRequestDto requestDto = new LogRequestDto(userId, targetType, startAt, endAt);


        Page<Log> logs = logRepository.findByFilter(requestDto, pageable);

        return logs.map(log -> new LogResponseDto(
                log.getId(),
                log.getUserId(),
                log.getTargetType(),
                log.getTargetId(),
                log.getRequestMethod(),
                log.getRequestUri(),
                log.getActivityType(),
                log.getStatusCode(),
                log.getMessage(),
                log.getCreatedAt()
        ));
    }
}