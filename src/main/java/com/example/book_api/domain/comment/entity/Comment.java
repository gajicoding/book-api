package com.example.book_api.domain.comment.entity;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", length = 600, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book; // 책

    public Comment(){}

    public Comment(String content, User user, Book book) {
        this.content = content;
        this.user = user;
        this.book = book;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
