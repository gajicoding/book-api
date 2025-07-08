package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
}
