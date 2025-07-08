package com.example.book_api.domain.book.dto;


import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Year;

@Getter
@AllArgsConstructor
public class BookResponseDto {

    private final Long id;
    private final String title;
    private final String author;
    private final String publisher;
    private final Year publicationYear;
    private final Long isbn;
    private final CategoryEnum category;


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
