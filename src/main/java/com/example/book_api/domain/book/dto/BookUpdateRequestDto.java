package com.example.book_api.domain.book.dto;

import com.example.book_api.domain.book.enums.CategoryEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Year;

@Getter
@NoArgsConstructor
public class BookUpdateRequestDto {

    private String title;
    private String author;
    private String publisher;
    private Year publicationYear;
    private String isbn;
    private CategoryEnum category;
}
