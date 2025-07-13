package com.example.book_api.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id; // comment_id
    private String content;
    private Long userId;
    private Long bookId;
    private String userName;

    public CommentResponseDto(Long id, String content, Long userId, String userName, Long bookId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.bookId = bookId;
    }
}
