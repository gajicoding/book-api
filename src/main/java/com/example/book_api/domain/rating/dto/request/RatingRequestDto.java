package com.example.book_api.domain.rating.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class RatingRequestDto {

    @Min(1)
    @Max(5)
    private int score;
}
