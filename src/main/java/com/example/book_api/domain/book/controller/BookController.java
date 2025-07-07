package com.example.book_api.domain.book.controller;

import com.example.book_api.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
}
