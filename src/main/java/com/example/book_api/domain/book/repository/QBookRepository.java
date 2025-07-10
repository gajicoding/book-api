package com.example.book_api.domain.book.repository;

import com.example.book_api.domain.book.dto.BookTrendResponseDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.entity.QBook;
import com.example.book_api.domain.book.entity.QBookKeyword;
import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.book_api.domain.book.entity.QBook.book;
import static com.example.book_api.domain.book.entity.QBookView.bookView;
import static com.example.book_api.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class QBookRepository {

    private final JPAQueryFactory queryFactory;

    public List<Book> findTop10Books() {
        return queryFactory.select(book)
                .from(bookView)
                .join(bookView.book, book)
                .groupBy(book)
                .orderBy(bookView.count().desc())
                .limit(10)
                .fetch();
    }

    public List<Book> findTop10BooksByCategory(CategoryEnum categoryEnum) {
        return queryFactory.select(book)
                .from(bookView)
                .join(bookView.book, book)
                .groupBy(book)
                .where(book.category.eq(categoryEnum))
                .orderBy(bookView.count().desc())
                .limit(10)
                .fetch();
    }

    public List<Book> findTop10BooksByAgeGroup(AgeGroup ageGroup) {
        int currentYear = LocalDate.now().getYear();

        int minYear = currentYear - ageGroup.getMaxAge();
        int maxYear = currentYear - ageGroup.getMinAge();

        LocalDate minBirth = LocalDate.of(minYear, 1, 1);
        LocalDate maxBirth = LocalDate.of(maxYear, 12, 31);

        return queryFactory.select(book)
                .from(bookView)
                .join(bookView.book, book)
                .join(bookView.user, user)
                .where(user.birth.between(minBirth, maxBirth))
                .groupBy(book)
                .where()
                .orderBy(bookView.count().desc())
                .limit(10)
                .fetch();
    }


    public Page<Book> searchAllFields(String keyword, Pageable pageable) {
        QBook book = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();


        builder.or(book.title.containsIgnoreCase(keyword));
        builder.or(book.author.containsIgnoreCase(keyword));
        builder.or(book.publisher.containsIgnoreCase(keyword));

        List<Book> content = queryFactory.selectFrom(book).where(builder).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(book.id.desc()).fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(book)
                .where(builder)
                .fetchOne();


        return new PageImpl<>(content, pageable, total);
    }

    public List<Book> findAllByCursor(Long cursor, Long size) {
        QBook book = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();

        if (cursor != null) {
            builder.and(book.id.lt(cursor)); // cursor 보다 작은 id만
        }

        return queryFactory
                .selectFrom(book)
                .where(builder)
                .orderBy(book.id.desc()) // 내림차순
                .limit(size)
                .fetch();
    }


    public List<BookTrendResponseDto> findTrend() {
        QBookKeyword keyword = QBookKeyword.bookKeyword;
        return queryFactory
                .select(Projections.constructor( // dto로 전환
                        BookTrendResponseDto.class,
                        keyword.keyword,
                        keyword.count()
                ))
                .from(keyword)
                .groupBy(keyword.keyword)
                .orderBy(keyword.count().desc())
                .limit(10)
                .fetch();
    }
}
