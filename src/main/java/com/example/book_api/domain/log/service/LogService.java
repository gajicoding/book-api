package com.example.book_api.domain.log.service;

import com.example.book_api.domain.log.entity.Log;
import com.example.book_api.domain.log.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(Log log){
        logRepository.save(log);
    }
}