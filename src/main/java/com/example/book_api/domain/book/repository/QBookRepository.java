package com.example.book_api.domain.book.repository;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.entity.QBook;
import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.exception.InvalidSearchConditionException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
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
                .rightJoin(bookView.book, book)
                .groupBy(book)
                .orderBy(bookView.count().desc())
                .limit(10)
                .fetch();
    }

    public List<Book> findTop10BooksByCategory(CategoryEnum categoryEnum) {
        return queryFactory.select(book)
                .from(bookView)
                .rightJoin(bookView.book, book)
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
                .rightJoin(bookView.book, book)
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
        builder.or(book.publicationYear.eq(Year.parse(keyword)));
        builder.or(book.category.stringValue().containsIgnoreCase(keyword));

        JPAQuery<Book> query = queryFactory.selectFrom(book).where(builder);

        long total = queryFactory.select(Wildcard.count).from(book).where(builder).fetchOne();


        List<Book> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(book.id.desc()).fetch();

        if (content.isEmpty()) {
            throw new InvalidSearchConditionException(HttpStatus.NOT_FOUND,"키워드에 해당하는 책이 없습니다. 다시 입력해주세요.");
        }

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
}
