package com.example.book_api.domain.rating.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingDistributionResponse {
    private int score;
    private long count;
}
