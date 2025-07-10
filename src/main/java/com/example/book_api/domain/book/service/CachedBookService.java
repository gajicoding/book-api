package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CachedBookService {

    private final BookService bookService;

    @Cacheable(value = "bookTop", key = "'all'")
    public List<BookResponseDto> getTopBooksCached() {
        return bookService.getTopBooks();
    }

    @Cacheable(value = "bookTop", key = "'category_' + #category")
    public List<BookResponseDto> getTopBookByCategoryCached(String category) {
        return bookService.getTopBookByCategory(category);
    }

    @Cacheable(value = "bookTop", key = "'ageGroup_' + #ageGroup")
    public List<BookResponseDto> getTopBookByUserAgeCached(String ageGroup) {
        return bookService.getTopBookByUserAge(ageGroup);
    }
}
