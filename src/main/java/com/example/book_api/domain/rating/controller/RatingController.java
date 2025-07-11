package com.example.book_api.domain.rating.controller;

import com.example.book_api.domain.rating.dto.request.RatingRequestDto;
import com.example.book_api.domain.rating.dto.response.AverageRatingResponse;
import com.example.book_api.domain.rating.dto.response.MyRatingResponse;
import com.example.book_api.domain.rating.dto.response.RatingDistributionResponse;
import com.example.book_api.domain.rating.dto.response.TopRatedBookResponse;
import com.example.book_api.domain.rating.service.RatingService;
import com.example.book_api.domain.auth.annotation.Auth;
import com.example.book_api.domain.auth.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/books/{bookId}/ratings")
    public void createRating(@PathVariable Long bookId,
                             @Valid @RequestBody RatingRequestDto request,
                             @Auth AuthUser authUser) {
        ratingService.createRating(bookId, request, authUser.getId());
    }

    @PatchMapping("/books/{bookId}/ratings")
    public void updateRating(@PathVariable Long bookId,
                             @Valid @RequestBody RatingRequestDto request,
                             @Auth AuthUser authUser) {
        ratingService.updateRating(bookId, request, authUser.getId());
    }

    @DeleteMapping("/books/{bookId}/ratings")
    public void deleteRating(@PathVariable Long bookId,
                             @Auth AuthUser authUser) {
        ratingService.deleteRating(bookId, authUser.getId());
    }

    //내가 남긴 평점 조회
    @GetMapping("/books/{bookId}/ratings/me")
    public MyRatingResponse getMyRating(@PathVariable Long bookId,
                                        @Auth AuthUser authUser) {
        return ratingService.getMyRating(bookId, authUser.getId());
    }

    //책 평점 평균 조회
    @GetMapping("/books/{bookId}/ratings")
    public AverageRatingResponse getAverageRating(@PathVariable Long bookId) {
        return ratingService.getAverageRating(bookId);
    }

    //책 평점 분포 조회
    @GetMapping("/books/{bookId}/ratings/distribution")
    public List<RatingDistributionResponse> getDistribution(@PathVariable Long bookId) {
        return ratingService.getRatingDistribution(bookId);
    }

    //평점 높은 순 top 10
    @GetMapping("/books/top/ratings")
    public List<TopRatedBookResponse> getTopRatedBooks() {
        return ratingService.getTop10RatedBooks();
    }

}
