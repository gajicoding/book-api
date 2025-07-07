package com.example.book_api.domain.log.entity;

import com.example.book_api.domain.log.enums.RequestMethod;
import com.example.book_api.domain.log.enums.TargetType;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TargetType targetType;

    private Long targetId;

    @Enumerated(value = EnumType.STRING)
    private RequestMethod requestMethod;

    private String requestUri;

    private String message;

    private boolean isSuccess;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // user와 관계 추후 설정
    // @ManyToOne(fetch = FetchType.LAZY)
    // private User user;


    // 생성자 추후 추가
}
