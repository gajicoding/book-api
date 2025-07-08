package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookRegistResquestDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponseDto regist(BookRegistResquestDto resquestDto) {
        Book newBook = resquestDto.toEntity();
        Book regist = bookRepository.save(newBook);
        return new BookResponseDto(regist);
    }
}
