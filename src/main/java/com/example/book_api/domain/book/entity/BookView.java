package com.example.book_api.domain.book.entity;

import com.example.book_api.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "book_views", indexes = {
        @Index(name = "idx_book_views_book_id_id", columnList = "id, book_id")
})
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BookView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public BookView(Book book, User user) {
        this.book = book;
        this.user = user;
    }
}