package com.example.book_api.domain.book.controller;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookRegistResquestDto;
import com.example.book_api.domain.book.dto.BookUpdateRequestDto;
import com.example.book_api.domain.book.service.BookService;
import com.example.book_api.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<ApiResponse<BookResponseDto>> regist(
            @RequestBody BookRegistResquestDto resquestDto) {

        return ApiResponse.success(HttpStatus.OK, "책이 등록되었습니다.", bookService.regist(resquestDto));
    }

    // 단건 조회
    @GetMapping("/books/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> find(
            @PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK, "성공적으로 조회되었습니다.", bookService.find(id));
    }


    @GetMapping("/books")
    public ResponseEntity<ApiResponse<Page<BookResponseDto>>> findAll(Pageable pageable) {
        Page<BookResponseDto> page = bookService.findAll(pageable);
        return ApiResponse.success(
                HttpStatus.OK, "성공적으로 조회되었습니다.", page);
    }

    @PatchMapping("/books/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> update(
            @PathVariable Long id,
            @RequestBody BookUpdateRequestDto requestDto
    ) {
        return ApiResponse.success(HttpStatus.OK, "책이 수정되었습니다.", bookService.update(id, requestDto));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<ApiResponse<LocalDateTime>> delete(
            @PathVariable Long id
    ) {
        return ApiResponse.success(HttpStatus.OK,"책이 삭제 되었습니다.", bookService.softDel(id));
    }
}
