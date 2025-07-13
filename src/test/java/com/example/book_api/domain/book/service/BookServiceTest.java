package com.example.book_api.domain.book.service;


import com.example.book_api.domain.auth.dto.AuthUser;
import com.example.book_api.domain.book.dto.BookRegistRequestDto;
import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.entity.BookKeyword;
import com.example.book_api.domain.book.repository.BookRepository;
import com.example.book_api.domain.book.repository.QBookRepository;
import com.example.book_api.domain.user.enums.Role;
import com.example.book_api.global.dto.PagedResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    QBookRepository qBookRepository;
    @InjectMocks
    BookService bookService;

    @Test
    void regist_정상등록() {
        //given 가짜 객체 생성
        BookRegistRequestDto requestDto = mock(BookRegistRequestDto.class);
        Book mockBook = mock(Book.class);
        AuthUser authUser = new AuthUser(1L, "test@gmail.com", "test", Role.ADMIN);
        //given 메서드 호출시 무조건 mockBook 리턴해라
        when(requestDto.toEntity()).thenReturn(mockBook);
        when(bookRepository.save(mockBook)).thenReturn(mockBook);

        //when
        BookResponseDto responseDto = bookService.regist(requestDto, authUser);

        //then
        assertNotNull(responseDto);
    }

    @Test
    void findAllBykeyword_조회성공() {
        //given
        String keyword = "쓰다";
        when(qBookRepository.isPopularKeyword(any(String.class))).thenReturn(true);

        //when
        boolean result = bookService.isPopularKeyword(keyword);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void findAll_키워드가_없으면_findAll_호출된다() {
        // given
        int page = 1;
        int size = 10;
        String keyword = "쓰다";
        AuthUser authUser = new AuthUser(1L, "test@gmail.com", "test", Role.ADMIN);
        Page<Book> bookPage = mock(Page.class);

        when(bookService.saveKeyword(keyword, authUser)).thenReturn(null);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        when(bookPage.map(any())).thenReturn(mock(Page.class));

        // when
        PagedResponse result = bookService.findAll(page, size, keyword, authUser);

        // then
        verify(bookRepository).findAll(any(Pageable.class));
        verify(qBookRepository, never()).searchAllFields(anyString(), any(Pageable.class));
        assertNotNull(result);
    }

    @Test
    void findAll_키워드가_있으면_searchAllFields_호출된다() {
        // given
        int page = 1;
        int size = 10;
        String keyword = "쓰다";
        AuthUser authUser = new AuthUser(1L, "test@gmail.com", "test", Role.ADMIN); // 추가
        Page<Book> bookPage = mock(Page.class);
        BookKeyword bookKeyword = mock(BookKeyword.class);

        when(bookService.saveKeyword(keyword, authUser)).thenReturn(bookKeyword);
        when(qBookRepository.searchAllFields(eq(keyword), any(Pageable.class))).thenReturn(bookPage);
        when(bookPage.map(any())).thenReturn(mock(Page.class));

        // when
        PagedResponse result = bookService.findAll(page, size, keyword, authUser);

        // then
        verify(qBookRepository).searchAllFields(eq(keyword), any(Pageable.class));
        verify(bookRepository, never()).findAll(any(Pageable.class));
        assertNotNull(result);
    }


}
