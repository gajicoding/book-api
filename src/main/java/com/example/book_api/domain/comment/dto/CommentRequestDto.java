package com.example.book_api.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "null/공백은 입력 불가합니다.")
    @Size(min = 2, max = 300, message = "2~300 글자 내로 작성해야 합니다.")
    private String content;

    public CommentRequestDto(String content) {
        this.content = content;
    }
}
