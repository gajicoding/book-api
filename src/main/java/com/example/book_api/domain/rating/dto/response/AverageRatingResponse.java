package com.example.book_api.domain.rating.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AverageRatingResponse {
    private Long bookId;
    private double averageScore;
    private long count;
}
