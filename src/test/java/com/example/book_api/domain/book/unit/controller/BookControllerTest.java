package com.example.book_api.domain.book.unit.controller;

import com.example.book_api.domain.book.controller.BookController;
import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Year;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void 책_전체_TOP_10_조회() throws Exception {

        // given
        when(bookService.getTopBooks()).thenReturn(createMockBookResponseDto());

        // when
        ResultActions result = mockMvc.perform(
                get("/v1/books/top")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void 책_카테고리별_TOP_10_조회() throws Exception {

        // given
        when(bookService.getTopBookByCategory("GENERAL")).thenReturn(createMockBookResponseDto());

        // when
        ResultActions result = mockMvc.perform(
                get("/v1/books/top/categories")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void 책_나이대별_TOP_10_조회() throws Exception {

        // given
        when(bookService.getTopBookByUserAge("TEENS_EARLY")).thenReturn(createMockBookResponseDto());

        // when
        ResultActions result = mockMvc.perform(
                get("/v1/books/top/ages")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    private List<BookResponseDto> createMockBookResponseDto() {
        return List.of(
                new BookResponseDto(1L, "title1", "author1", "publisher1", Year.parse("2010"), "11111111", CategoryEnum.GENERAL),
                new BookResponseDto(2L, "title2", "author1", "publisher1", Year.parse("2010"), "11111111", CategoryEnum.GENERAL),
                new BookResponseDto(3L, "title3", "author1", "publisher1", Year.parse("2010"), "11111111", CategoryEnum.GENERAL)
        );
    }
}
