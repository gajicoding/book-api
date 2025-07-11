package com.example.book_api.domain.rating.repository;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.rating.dto.response.RatingDistributionResponse;
import com.example.book_api.domain.rating.dto.response.TopRatedBookResponse;
import com.example.book_api.domain.rating.entity.Rating;
import com.example.book_api.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByBookAndUser(Book book, User user);

    boolean existsByBookAndUser(Book book, User user);

    //평점 계산
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.book = :book")
    Double findAverageByBook(@Param("book") Book book);

    long countByBook(Book book);

    //책 평점 분포
    @Query("SELECT r.score, COUNT(r) FROM Rating r WHERE r.book = :book GROUP BY r.score")
    List<RatingDistributionResponse> countGroupByScore(@Param("book") Book book);

    //평점 탑10
    @Query("""
        SELECT r.book.id, r.book.title, AVG(r.score), COUNT(r)
        FROM Rating r
        GROUP BY r.book.id, r.book.title
        ORDER BY AVG(r.score) DESC
        """)
    List<TopRatedBookResponse> findTop10BooksByAverageScore(Pageable pageable);
}

