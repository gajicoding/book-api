package com.example.book_api.domain.rating.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopRatedBookResponse {
    private Long bookId;
    private String title;
    private double averageScore;
    private long ratingCount;
}