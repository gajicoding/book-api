package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookRegistResquestDto;
import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.exception.NotFoundException;
import com.example.book_api.domain.book.repository.BookRepository;
import com.example.book_api.domain.book.repository.QBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponseDto regist(BookRegistResquestDto resquestDto) {
        Book newBook = resquestDto.toEntity();
        Book regist = bookRepository.save(newBook);
        return new BookResponseDto(regist);
    }

    // 단건 조회
    public BookResponseDto find(Long id) {
        Book byId = bookRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(HttpStatus.NOT_FOUND,
                        "해당 id로 책을 찾을 수 없습니다. 다른 id를 입력해주세요!"));
        return new BookResponseDto(byId);
    }


    private final QBookRepository qBookRepository;

    // 책 전체 top 10
    public List<BookResponseDto> getTopBooks() {
        return qBookRepository.findTop10Books()
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }

    // 책 카테고리별 top 10
    public List<BookResponseDto> getTopBookByCategory(String category) {
        CategoryEnum categoryEnum = CategoryEnum.of(category).orElseThrow(RuntimeException::new);

        return qBookRepository.findTop10BooksByCategory(categoryEnum)
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }

    // 책 나이대 별 top 10
    public List<BookResponseDto> getTopBookByUserAge(String ageGroup) {
        AgeGroup ageGroupEnum = AgeGroup.of(ageGroup).orElseThrow(RuntimeException::new);

        return qBookRepository.findTop10BooksByAgeGroup(ageGroupEnum)
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }

}
