package com.example.book_api.domain.book.service;


import com.example.book_api.domain.book.dto.BookRegistRequestDto;
import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.repository.BookRepository;
import com.example.book_api.domain.book.repository.QBookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;


@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    QBookRepository qBookRepository;
    @Mock
    BookViewService bookViewService;
    @Mock
    BookKeywordService bookKeywordService;
    @InjectMocks
    BookService bookService;

    @Test
    void regist_정상등록() {
        //given // 가짜 객체 생성
        BookRegistRequestDto requestDto = Mockito.mock(BookRegistRequestDto.class);
        Book mockBook = Mockito.mock(Book.class);
        //given // 메서드 호출시 무조건 mockBook 리턴해라
        Mockito.when(requestDto.toEntity()).thenReturn(mockBook);
        Mockito.when(bookRepository.save(mockBook)).thenReturn(mockBook);

        //when
        BookResponseDto responseDto = bookService.regist(requestDto);

        //then
        assertNotNull(responseDto);
    }

    @Test
    void find_책찾기() {
        //given
        Long mockId = 1L;
        Book mockBook = Mockito.mock(Book.class);
        //when
        //then
    }


}
