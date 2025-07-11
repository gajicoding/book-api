package com.example.book_api.domain.book.dto;


import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.enums.CategoryEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Year;

@RequiredArgsConstructor
@Getter
public class BookRegistRequestDto {

    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    private String publisher;
    @NotNull
    private Year publicationYear;
    @NotNull
    private String isbn;
    @NotNull
    private CategoryEnum category;

    public Book toEntity() {
        return new Book(title, author, publisher, publicationYear, isbn, category);
    }
}
