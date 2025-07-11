package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookTrendResponseDto;
import com.example.book_api.domain.book.validation.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CachedBoardService {

    private final BookService bookService;
    private final BookValidator bookValidator;

    @Cacheable(value = "bookTop", key = "'all'")
    public List<BookResponseDto> getTopBooksCached() {
        return bookService.getTopBooks();
    }

    @Cacheable(value = "bookTop", key = "'category_' + #category")
    public List<BookResponseDto> getTopBookByCategoryCached(String category) {
        bookValidator.validateCategory(category);

        return bookService.getTopBookByCategory(category);
    }

    @Cacheable(value = "bookTop", key = "'ageGroup_' + #ageGroup")
    public List<BookResponseDto> getTopBookByUserAgeCached(String ageGroup) {
        bookValidator.validateAgeGroup(ageGroup);

        return bookService.getTopBookByUserAge(ageGroup);
    }


}
