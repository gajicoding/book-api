package com.example.book_api.domain.rating.entity;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ratings")
@Getter
@NoArgsConstructor
public class Rating extends BaseEntity {

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

    @Builder
    public Rating(int score, Book book, User user) {
        this.score = score;
        this.book = book;
        this.user = user;
    }

    public void updateScore(int score) {
        this.score = score;
    }
}
