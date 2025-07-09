package com.example.book_api.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id; // comment_id
    private String content;
    private Long userId;
    private Long bookId;

    public CommentResponseDto(Long id, String content, Long userId, Long bookId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.bookId = bookId;

    }
}
