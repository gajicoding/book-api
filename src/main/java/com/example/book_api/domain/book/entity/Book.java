package com.example.book_api.domain.book.entity;

import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Year;

@Entity
@Getter
@Table(name = "books")
@NoArgsConstructor
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String author;
    private String publisher;
    private Year publicationYear;
    private Long isbn;

    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    public Book(String title, String author, String publisher, Year publicationYear, Long isbn, CategoryEnum category) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.category = category;
    }


    public void updateUser(User user) {
        this.user = user;
    }
}
