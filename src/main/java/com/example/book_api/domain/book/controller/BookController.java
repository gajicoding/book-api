package com.example.book_api.domain.book.controller;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookRegistResquestDto;
import com.example.book_api.domain.book.service.BookService;
import com.example.book_api.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<ApiResponse<BookResponseDto>> regist(
            @RequestBody BookRegistResquestDto resquestDto) {

        return ApiResponse.success(HttpStatus.OK,"성공", bookService.regist(resquestDto));
    }

    
}
