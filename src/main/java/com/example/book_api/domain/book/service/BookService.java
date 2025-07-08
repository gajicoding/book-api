package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.repository.BookRepository;
import com.example.book_api.domain.book.repository.QBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final QBookRepository qBookRepository;

    // 책 전체 top
    public List<BookResponseDto> getTopBooks() {
        return qBookRepository.findTop10BooksByViewCount()
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }


}
