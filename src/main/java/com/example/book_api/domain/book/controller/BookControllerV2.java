package com.example.book_api.domain.book.controller;

import com.example.book_api.domain.auth.dto.AuthUser;
import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.service.BookService;
import com.example.book_api.domain.book.service.CachedBookService;
import com.example.book_api.global.dto.ApiResponse;
import com.example.book_api.global.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2")
@RequiredArgsConstructor
public class BookControllerV2 {
    private final CachedBookService cachedBookService;
    private final BookService bookService;

    // 책 전체 top 10
    @GetMapping("/books/top")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getTopBooks() {
        return ApiResponse.success(
                HttpStatus.OK, "책 전체 top 10 조회가 완료되었습니다.", cachedBookService.getTopBooksCached()
        );
    }

    // 책 카테고리별 top 10
    @GetMapping("/books/top/categories")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getTopBookByCategory(
            @RequestParam(defaultValue = "GENERAL") String category
    ) {
        return ApiResponse.success(
                HttpStatus.OK, "책 카테고리별 top 10 조회가 완료되었습니다.", cachedBookService.getTopBookByCategoryCached(category)
        );
    }

    // 책 나이대 별 top 10
    @GetMapping("/books/top/ages")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getTopBookByUserAge(
            @RequestParam(defaultValue = "TEENS_EARLY") String ageGroup
    ) {
        return ApiResponse.success(
                HttpStatus.OK, "책 나이대 별 top 10 조회가 완료되었습니다.", cachedBookService.getTopBookByUserAgeCached(ageGroup)
        );
    }

    // 책전체 조회
    @GetMapping("/books")
    public ResponseEntity<ApiResponse<PagedResponse<BookResponseDto>>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal AuthUser authUser) {

        PagedResponse<BookResponseDto> result;

        if(bookService.isPopularKeyword(keyword)) {
            result = cachedBookService.findAllCached(page, size, keyword, authUser);
        } else {
            result = bookService.findAll(page, size, keyword, authUser);
        }

        return ApiResponse.success(
                HttpStatus.OK, "성공적으로 조회되었습니다.", result
        );
    }

}
