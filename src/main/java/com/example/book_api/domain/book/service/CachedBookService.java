package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.validation.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CachedBookService {

    private final BookService bookService;
    private final BookValidator bookValidator;

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

    // Book 검색:
    // Qrepository 쿼리
    // keyword 가 인기 keyword 목록에 있는지 확인
    // 있으면 -> 캐시적용
    // 없으면 -> service, repository , DB 참조

    // 페이징 모두 적용
}
