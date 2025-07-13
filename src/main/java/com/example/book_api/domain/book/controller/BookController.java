package com.example.book_api.domain.book.controller;

import com.example.book_api.domain.auth.dto.AuthUser;
import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookRegistRequestDto;
import com.example.book_api.domain.book.dto.BookTrendResponseDto;
import com.example.book_api.domain.book.dto.BookUpdateRequestDto;
import com.example.book_api.domain.book.service.BookService;
import com.example.book_api.domain.log.enums.ActivityType;
import com.example.book_api.global.annotation.Logging;
import com.example.book_api.global.dto.ApiResponse;
import com.example.book_api.global.dto.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 책 등록
    @Logging(activityType = ActivityType.BOOK_CREATED)
    @PostMapping("/books")
    public ResponseEntity<ApiResponse<BookResponseDto>> regist(
            @RequestBody @Valid BookRegistRequestDto resquestDto,
            @AuthenticationPrincipal AuthUser authUser) {

        return ApiResponse.success(HttpStatus.OK, "책이 등록되었습니다.", bookService.regist(resquestDto, authUser));
    }

    // 책 단건 조회
    @GetMapping("/books/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> find(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.success(HttpStatus.OK,"성공적으로 조회되었습니다.", bookService.find(id, authUser));
    }

    // 책 전체 조회
    @GetMapping("/books")
    public ResponseEntity<ApiResponse<PagedResponse<BookResponseDto>>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal AuthUser authUser) {

        return ApiResponse.success(
                HttpStatus.OK, "성공적으로 조회되었습니다.", bookService.findAll(page, size, keyword, authUser)
        );
    }

    // 책 전체 조회 cursor방식
    @GetMapping("/books/cursor")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> findAllByCursor(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") Long size
    ) {
        return ApiResponse.success(
                HttpStatus.OK,"성공적으로 조회되었습니다.", bookService.findAllByCursor(cursor, size)
        );
    }

    //인기keyword 검색
    @GetMapping("/books/trend")
    public ResponseEntity<ApiResponse<List<BookTrendResponseDto>>> trend() {
        return ApiResponse.success(HttpStatus.OK,"인기 keyword 검색이 완료되었습니다.", bookService.findKeyword());
    }

    // 책 수정
    @Logging(activityType = ActivityType.BOOK_UPDATED)
    @PatchMapping("/books/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> update(
            @PathVariable Long id,
            @RequestBody BookUpdateRequestDto requestDto
    ) {
        return ApiResponse.success(
                HttpStatus.OK, "책이 수정되었습니다.", bookService.update(id, requestDto)
        );
    }

    // 책 삭제
    @Logging(activityType = ActivityType.BOOK_DELETED)
    @DeleteMapping("/books/{id}")
    public ResponseEntity<ApiResponse<LocalDateTime>> delete(
            @PathVariable Long id
    ) {
        return ApiResponse.success(HttpStatus.OK,"책이 삭제 되었습니다.", bookService.softDel(id));
    }

    // 책 전체 top 10
    @GetMapping("/books/top")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getTopBooks() {
        return ApiResponse.success(
                HttpStatus.OK, "책 전체 top 10 조회가 완료되었습니다.", bookService.getTopBooks()
        );
    }

    // 책 카테고리별 top 10
    @GetMapping("/books/top/categories")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getTopBookByCategory(
            @RequestParam(defaultValue = "GENERAL") String category
    ) {
        return ApiResponse.success(
                HttpStatus.OK, "책 카테고리별 top 10 조회가 완료되었습니다.", bookService.getTopBookByCategory(category)
        );
    }

    // 책 나이대 별 top 10
    @GetMapping("/books/top/ages")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getTopBookByUserAge(
            @RequestParam(defaultValue = "TEENS_EARLY") String ageGroup
    ) {
        return ApiResponse.success(
                HttpStatus.OK, "책 나이대 별 top 10 조회가 완료되었습니다.", bookService.getTopBookByUserAge(ageGroup)
        );
    }
}
