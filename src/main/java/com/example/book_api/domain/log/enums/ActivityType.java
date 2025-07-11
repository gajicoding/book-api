package com.example.book_api.domain.log.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    USER_REGISTERED("회원가입"),
    USER_LOG_IN("로그인"),
    USER_ROLE_UPDATED("유저 권한 수정"),
    USER_PASSWORD_UPDATED("유저 비밀번호 수정"),
    USER_DELETED("회원탈퇴"),
    BOOK_CREATED("책 등록"),
    BOOK_UPDATED("책 수정"),
    BOOK_DELETED("책 삭제"),
    COMMENT_CREATED("댓글 등록"),
    COMMENT_UPDATED("댓글 수정"),
    COMMENT_DELETED("댓글 삭제"),
    RATING_CREATED("평점 등록"),
    RATING_UPDATED("평점 수정"),
    RATING_DELETED("평점 삭제");


    private final String typeDescription;

    ActivityType(String typeDescription) {
        this.typeDescription = typeDescription;
    }
}
