package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.validation.BookValidator;
import com.example.book_api.global.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    @Cacheable(value = "books", key = "#keyword + #page + ',' + #size")
    public PagedResponse<BookResponseDto> findAllCached(int page, int size, String keyword) {
        return bookService.findAll(page, size, keyword);
    }

//    @Cacheable(value = "books", key = "#keyword + '_' + #page + '_' + #size")
//    public Page<BookResponseDto> findAll(int page, int size, String keyword) {
//        return bookService.findAll(page, size, keyword);
//    }

    // Book 검색:
    // Qrepository 쿼리
    // keyword 가 인기 keyword 목록에 있는지 확인
    // 있으면 -> 캐시적용
    // 없으면 -> service, repository , DB 참조

    // 페이징 모두 적용


}
