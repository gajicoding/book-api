package com.example.book_api.domain.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookTrendResponseDto {

    private String keyword;
    private Long keywordCount;


}
