package com.example.book_api.domain.book.repository;

import com.example.book_api.domain.book.entity.Book;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.book_api.domain.book.entity.QBook.book;
import static com.example.book_api.domain.book.entity.QBookView.bookView;

@Repository
@RequiredArgsConstructor
public class QBookRepository {

    private final JPAQueryFactory queryFactory;

    public List<Book> findTop10BooksByViewCount() {
        return queryFactory.select(book)
                .from(bookView)
                .join(bookView.book, book)
                .groupBy(book)
                .orderBy(bookView.count().desc())
                .limit(10)
                .fetch();
    }
}
