package com.example.book_api.domain.book.service;

import com.example.book_api.domain.auth.dto.AuthUser;
import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.global.dto.PagedResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CachedBookService {

    private final BookService bookService;
    private final BookKeywordService bookKeywordService;

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, PagedResponse<BookResponseDto>> redisTemplate;


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


//    @Cacheable(value = "books", key = "(#keyword != null ? #keyword : '') + #page + ',' + #size")
//    public PagedResponse<BookResponseDto> findAllCached(int page, int size, String keyword) {
//        return bookService.findAll(page, size, keyword);
//    }

    // 역 직렬화 문제로 수동 캐싱
    public PagedResponse<BookResponseDto> findAllCached(int page, int size, String keyword, AuthUser authUser) {

        // key 조회
        String key = "books::" + (keyword != null ? keyword : "") + "." + page + "." + size;
        Object cached = redisTemplate.opsForValue().get(key);
        PagedResponse<BookResponseDto> cachedResponse = objectMapper.convertValue(
                cached,
                new TypeReference<PagedResponse<BookResponseDto>>() {}
        );

        // 저장된 캐시가 있으면, 캐시 값 출력
        if (cachedResponse != null) {
            // 검색어 저장
            bookService.saveKeyword(keyword, authUser);
            return cachedResponse;
        }

        // 저장된 캐시가 없으면, DB 조회 + 캐시 생성
        PagedResponse<BookResponseDto> result = bookService.findAll(page, size, keyword, authUser);
        redisTemplate.opsForValue().set(key, result, 30, TimeUnit.MINUTES);

        return result;
    }


    // Book 검색:
    // Qrepository 쿼리
    // keyword 가 인기 keyword 목록에 있는지 확인
    // 있으면 -> 캐시적용
    // 없으면 -> service, repository , DB 참조
    // 페이징 모두 적용




}
