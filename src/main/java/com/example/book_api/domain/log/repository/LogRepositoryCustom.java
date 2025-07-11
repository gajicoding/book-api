package com.example.book_api.domain.log.repository;

import com.example.book_api.domain.log.dto.LogRequestDto;
import com.example.book_api.domain.log.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogRepositoryCustom {
    Page<Log> findByFilter(LogRequestDto logRequestDto, Pageable pageable);
}
