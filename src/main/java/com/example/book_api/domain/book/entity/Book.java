package com.example.book_api.domain.book.entity;

import com.example.book_api.domain.book.dto.BookUpdateRequestDto;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.Year;

@Entity
@Getter
@Setter
@Where(clause = "deleted_at IS NULL")
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
    private String isbn;

    @Enumerated(EnumType.STRING)
    private CategoryEnum category;


    public Book(String title, String author, String publisher, Year publicationYear, String isbn, CategoryEnum category) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.category = category;
    }


    public void updatePost(BookUpdateRequestDto requestDto) { // dto로 들어온 값만 수정하고 그 외의 값은 유지
        if (requestDto.getTitle() != null) this.title = requestDto.getTitle();
        if (requestDto.getAuthor() != null) this.author = requestDto.getAuthor();
        if (requestDto.getPublisher() != null) this.publisher = requestDto.getPublisher();
        if (requestDto.getPublicationYear() != null) this.publicationYear = requestDto.getPublicationYear();
        if (requestDto.getIsbn() != null) this.isbn = requestDto.getIsbn();
        if (requestDto.getCategory() != null) this.category = requestDto.getCategory();
    }


}
