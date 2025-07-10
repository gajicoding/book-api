package com.example.book_api.domain.rating.controller;

import com.example.book_api.domain.rating.dto.request.RatingRequestDto;
import com.example.book_api.domain.rating.dto.response.AverageRatingResponse;
import com.example.book_api.domain.rating.dto.response.MyRatingResponse;
import com.example.book_api.domain.rating.dto.response.RatingDistributionResponse;
import com.example.book_api.domain.rating.dto.response.TopRatedBookResponse;
import com.example.book_api.domain.rating.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public void createRating(@PathVariable Long bookId,
                             @Valid @RequestBody RatingRequestDto request,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        ratingService.createRating(bookId, request, userDetails.getUserId());
    }

    @PatchMapping
    public void updateRating(@PathVariable Long bookId,
                             @Valid @RequestBody RatingRequestDto request,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        ratingService.updateRating(bookId, request, userDetails.getUserId());
    }

    @DeleteMapping
    public void deleteRating(@PathVariable Long bookId,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        ratingService.deleteRating(bookId, userDetails.getUserId());
    }

    //내가 남긴 평점 조회
    @GetMapping("/books/{bookId}/ratings/me")
    public MyRatingResponse getMyRating(@PathVariable Long bookId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ratingService.getMyRating(bookId, userDetails.getUserId());
    }

    //책 평점 평균 조회
    @GetMapping("/v1/books/{bookId}/ratings")
    public AverageRatingResponse getAverageRating(@PathVariable Long bookId) {
        return ratingService.getAverageRating(bookId);
    }

    //책 평점 분포 조회
    @GetMapping("/v1/books/{bookId}/ratings/distribution")
    public List<RatingDistributionResponse> getDistribution(@PathVariable Long bookId) {
        return ratingService.getRatingDistribution(bookId);
    }

    //평점 높은 순 top 10
    @GetMapping("/v1/books/top/ratings")
    public List<TopRatedBookResponse> getTopRatedBooks() {
        return ratingService.getTop10RatedBooks();
    }

}
