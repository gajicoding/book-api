package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookRegistResquestDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.exception.NotFoundException;
import com.example.book_api.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    // 단건 조회
    public BookResponseDto find(Long id) {
        Book byId = bookRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(HttpStatus.NOT_FOUND,
                        "해당 id로 책을 찾을 수 없습니다. 다른 id를 입력해주세요!"));
        return new BookResponseDto(byId);
    }


}
