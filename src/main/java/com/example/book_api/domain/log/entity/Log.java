package com.example.book_api.domain.log.entity;

import com.example.book_api.domain.log.enums.ActivityType;
import com.example.book_api.domain.log.enums.RequestMethod;
import com.example.book_api.domain.log.enums.TargetType;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.service.UserService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "logs")
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(value = EnumType.STRING)
    private TargetType targetType;

    private Long targetId;

    @Enumerated(value = EnumType.STRING)
    private RequestMethod requestMethod;

    private String requestUri;

    private ActivityType activityType;

    private int statusCode;

    private String message;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Builder
    public Log(
            Long userId,
            TargetType targetType,
            Long targetId,
            RequestMethod requestMethod,
            String requestUri,
            ActivityType activityType,
            int statusCode,
            String message
    ) {
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.requestMethod = requestMethod;
        this.requestUri = requestUri;
        this.activityType = activityType;
        this.statusCode = statusCode;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
