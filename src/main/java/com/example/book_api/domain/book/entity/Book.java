package com.example.book_api.domain.book.entity;

import com.example.book_api.domain.book.categoryenum.CategoryEnum;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "books")
@NoArgsConstructor
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String title;
    private String author;
    private String publisher;
    private LocalDate publicationYear;
    private Long isbn;

    @Enumerated(EnumType.STRING)
    private CategoryEnum category;


}
