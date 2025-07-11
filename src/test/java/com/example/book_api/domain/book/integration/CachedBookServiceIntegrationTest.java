package com.example.book_api.domain.book.integration;

import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.service.BookService;
import com.example.book_api.domain.book.service.CachedBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import static org.mockito.Mockito.*;

@SpringBootTest
@EnableCaching
class CachedBookServiceIntegrationTest {

    @Autowired
    CachedBookService cachedBookService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @MockitoBean
    BookService bookService;

    @BeforeEach
    public void clearRedis() {
        try (var connection = redisConnectionFactory.getConnection()) {
            connection.execute("FLUSHDB");  // 현재 연결된 Redis DB 초기화
        }
    }

    @Test
    void 책_전체_Top_10_캐싱_테스트() {

        // when
        cachedBookService.getTopBooksCached();
        cachedBookService.getTopBooksCached();

        // then
        verify(bookService, times(1)).getTopBooks();
    }

    @Test
    void 책_카테고리별_Top_10_캐싱_테스트() {

        // when
        cachedBookService.getTopBookByCategoryCached(CategoryEnum.NATURAL_SCIENCE.name());
        cachedBookService.getTopBookByCategoryCached(CategoryEnum.NATURAL_SCIENCE.name());

        // then
        verify(bookService, times(1)).getTopBookByCategory(CategoryEnum.NATURAL_SCIENCE.name());
    }

    @Test
    void 책_나이대별_Top_10_캐싱_테스트() {

        // when
        cachedBookService.getTopBookByUserAgeCached(AgeGroup.TWENTIES_LATE.name());
        cachedBookService.getTopBookByUserAgeCached(AgeGroup.TWENTIES_LATE.name());

        // then
        verify(bookService, times(1)).getTopBookByUserAge(AgeGroup.TWENTIES_LATE.name());
    }
}
