package com.example.book_api.domain.book.controller;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookTrendResponseDto;
import com.example.book_api.domain.book.service.CachedBoardService;
import com.example.book_api.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2")
@RequiredArgsConstructor
public class BookControllerV2 {
    private final CachedBoardService cachedBoardService;

    // 책 전체 top 10
    @GetMapping("/books/top")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getTopBooks() {
        return ApiResponse.success(
                HttpStatus.OK, "책 전체 top 10 조회가 완료되었습니다.", cachedBoardService.getTopBooksCached()
        );
    }

    // 책 카테고리별 top 10
    @GetMapping("/books/top/categories")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getTopBookByCategory(
            @RequestParam(defaultValue = "GENERAL") String category
    ) {
        return ApiResponse.success(
                HttpStatus.OK, "책 카테고리별 top 10 조회가 완료되었습니다.", cachedBoardService.getTopBookByCategoryCached(category)
        );
    }

    // 책 나이대 별 top 10
    @GetMapping("/books/top/ages")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getTopBookByUserAge(
            @RequestParam(defaultValue = "TEENS_EARLY") String ageGroup
    ) {
        return ApiResponse.success(
                HttpStatus.OK, "책 나이대 별 top 10 조회가 완료되었습니다.", cachedBoardService.getTopBookByUserAgeCached(ageGroup)
        );
    }

    // 책전체 조회

}
