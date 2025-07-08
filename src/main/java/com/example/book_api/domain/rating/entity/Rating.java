package com.example.book_api.domain.rating.entity;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"book_id", "user_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Rating(int score, Book book, User user) {
        this.score = score;
        this.book = book;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateScore(int score) {
        this.score = score;
        this.updatedAt = LocalDateTime.now();
    }
}
