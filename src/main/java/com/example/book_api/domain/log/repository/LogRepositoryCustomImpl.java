package com.example.book_api.domain.log.repository;

import com.example.book_api.domain.log.dto.LogRequestDto;
import com.example.book_api.domain.log.entity.Log;
import com.example.book_api.domain.log.entity.QLog;
import com.example.book_api.domain.log.enums.TargetType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LogRepositoryCustomImpl implements LogRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QLog log = QLog.log;

    @Override
    public Page<Log> findByFilter(LogRequestDto requestDto, Pageable pageable) {

        List<Log> content = queryFactory
                .selectFrom(log)
                .where(
                        userIdEq(requestDto.getUserId()),
                        targetTypeEq(requestDto.getTargetType()),
                        createdAtBetween(requestDto.getStartAt(), requestDto.getEndAt())
                )
                .orderBy(log.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(log.count())
                .from(log)
                .where(
                        userIdEq(requestDto.getUserId()),
                        targetTypeEq(requestDto.getTargetType()),
                        createdAtBetween(requestDto.getStartAt(), requestDto.getEndAt())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, count != null ? count : 0);
    }



    // ===== 동적 쿼리를 위한 BooleanExpression 메서드 =====

    private BooleanExpression userIdEq(Long userId) {
        // userId가 null이 아니면 where 조건에 추가, null이면 무시
        return userId != null ? log.userId.eq(userId) : null;
    }

    private BooleanExpression targetTypeEq(String targetType) {
        // targetType이 null이 아니면 where 조건에 추가
        return targetType != null ? log.targetType.eq(TargetType.valueOf(targetType)) : null;
    }

    private BooleanExpression createdAtBetween(String startAt, String endAt) {
        LocalDateTime startDateTime = parseStartDate(startAt);
        LocalDateTime endDateTime = parseEndDate(endAt);

        // 시작일, 종료일 둘 다 있을 경우 사이에 있는 데이터 추가
        if (startAt != null && endAt != null) {
            return log.createdAt.between(startDateTime, endDateTime);
        } else if (startAt != null) {
            return log.createdAt.goe(startDateTime);
        } else if (endAt != null) {
            return log.createdAt.loe(endDateTime);
        }
        return null;
    }


    // ===== String -> LocalDateTime 변환을 위한 메서드 =====

    private LocalDateTime parseStartDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        return LocalDate.parse(dateStr).atStartOfDay(); // 00:00:00
    }

    private LocalDateTime parseEndDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        return LocalDate.parse(dateStr).atTime(LocalTime.MAX); // 23:59:59.999999999
    }
}
