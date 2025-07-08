package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.enums.CategoryEnum;
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

    // 책 전체 top 10
    public List<BookResponseDto> getTopBooks() {
        return qBookRepository.findTop10Books()
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }

    // 책 카테고리별 top 10
    public List<BookResponseDto> getTopBookByCategory(String category) {
        CategoryEnum categoryEnum = CategoryEnum.of(category).orElseThrow(RuntimeException::new);

        return qBookRepository.findTop10BooksByCategory(categoryEnum)
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }


}
