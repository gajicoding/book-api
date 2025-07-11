package com.example.book_api.domain.rating.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyRatingResponse {
    private Long bookId;
    private int score;
}
