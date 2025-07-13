package com.example.book_api.domain.book.integration;

import com.example.book_api.domain.book.enums.CategoryEnum;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class BookBulkInsertTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Disabled // 필요 시 주석 해제
    @Test
    @Transactional
    @Rollback(false)
    public void insertBulkBooksTest() {
        int batchSize = 1_000;
        List<Object[]> params = new ArrayList<>();

        CategoryEnum[] categories = CategoryEnum.values();

        for (int i = 0; i < 1_000_000; i++) {
            params.add(new Object[]{
                    "제목a"+i,
                    "작가a"+i,
                    "출판사a"+i,
                    2010+i%10,
                    i,
                    categories[i % categories.length].name()
            });

            if (params.size() == batchSize) {
                jdbcTemplate.batchUpdate(
                        "INSERT INTO books (title, author, publisher, publication_year, isbn, category, user_id, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?, 1, NOW(), NOW())",
                        params
                );
                params.clear();
            }
        }

        if (!params.isEmpty()) {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO books (title, author, publisher, publication_year, isbn, category, user_id, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?, 1, NOW(), NOW())",
                    params
            );
            params.clear();
        }
    }

    @Disabled // 필요 시 주석 해제
    @Test
    @Transactional
    @Rollback(false)
    public void insertBulkBookViewsTest() {
        int batchSize = 1_000;
        List<Object[]> params = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 10_000_000; i++) {
            int randomBookId = random.nextInt(1_000_000) + 1;

            params.add(new Object[]{
                    randomBookId
            });

            if (params.size() == batchSize) {
                jdbcTemplate.batchUpdate(
                        "INSERT INTO book_views (book_id, user_id, created_at) VALUES (?, 1, NOW())",
                        params
                );
                params.clear();
            }
        }

        if (!params.isEmpty()) {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO book_views (book_id, user_id, created_at) VALUES (?, 1, NOW())",
                    params
            );
            params.clear();
        }
    }
}
