package com.example.book_api.domain.book.unit.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.repository.BookRepository;
import com.example.book_api.domain.book.repository.QBookRepository;
import com.example.book_api.domain.book.service.BookService;
import com.example.book_api.domain.book.validation.BookValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepository bookRepository;
    @Mock
    QBookRepository qBookRepository;
    @Mock
    BookValidator bookValidator;

    @Test
    void 책_전체_Top_10_서비스_테스트() {
        // given
        when(qBookRepository.findTop10Books()).thenReturn(createMockBooks());

        // when
        List<BookResponseDto> result = bookService.getTopBooks();

        // then
        assertNotNull(result);
    }

    @Test
    void 책_카테고리별_Top_10_서비스_테스트() {
        // given
        when(bookValidator.validateCategory(CategoryEnum.GENERAL.name())).thenReturn(CategoryEnum.GENERAL);
        when(qBookRepository.findTop10BooksByCategory(CategoryEnum.GENERAL)).thenReturn(createMockBooks());

        // when
        List<BookResponseDto> result = bookService.getTopBookByCategory(CategoryEnum.GENERAL.name());

        // then
        assertNotNull(result);
    }

    @Test
    void 책_나이대별_Top_10_서비스_테스트() {
        // given
        when(bookValidator.validateAgeGroup(AgeGroup.TWENTIES_LATE.name())).thenReturn(AgeGroup.TWENTIES_LATE);
        when(qBookRepository.findTop10BooksByAgeGroup(AgeGroup.TWENTIES_LATE)).thenReturn(createMockBooks());

        // when
        List<BookResponseDto> result = bookService.getTopBookByUserAge(AgeGroup.TWENTIES_LATE.name());

        // then
        assertNotNull(result);
    }

    private List<Book> createMockBooks() {
        return List.of(
                new Book("title1", "author1", "publisher1", Year.parse("2010"), "11111111", CategoryEnum.GENERAL),
                new Book("title2", "author1", "publisher1", Year.parse("2010"), "11111111", CategoryEnum.GENERAL),
                new Book("title3", "author1", "publisher1", Year.parse("2010"), "11111111", CategoryEnum.GENERAL)
        );
    }
}
