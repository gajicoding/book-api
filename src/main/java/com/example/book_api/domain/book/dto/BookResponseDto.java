package com.example.book_api.domain.book.dto;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Year;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Year publicationYear;
    private String isbn;
    private CategoryEnum category;

    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.publicationYear = book.getPublicationYear();
        this.isbn = book.getIsbn();
        this.category = book.getCategory();
    }
}
