package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookRegistResquestDto;
import com.example.book_api.domain.book.dto.BookUpdateRequestDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.exception.InvalidSearchConditionException;
import com.example.book_api.domain.book.exception.NotFoundException;
import com.example.book_api.domain.book.repository.BookRepository;
import com.example.book_api.domain.book.repository.QBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final QBookRepository qBookRepository;

    // 책 등록
    public BookResponseDto regist(BookRegistResquestDto resquestDto) {
        Book newBook = resquestDto.toEntity();
        Book regist = bookRepository.save(newBook);
        return new BookResponseDto(regist);
    }

    // 책 단건 조회
    public BookResponseDto find(Long id) {
        Book byId = getBookById(id);
        return new BookResponseDto(byId);
    }

    // 책 전체 조회 page, size 방식
    public Page<BookResponseDto> findAll(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        Page<Book> books;
        if (keyword == null || keyword.trim().isEmpty()) {
            books = bookRepository.findAll(pageable);
        } else {
            books = qBookRepository.searchAllFields(keyword, pageable);
        }

        return books.map(book -> new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getIsbn(),
                book.getCategory()
        ));
    }

    // 책 cursor 방식으로 조회
    public List<BookResponseDto> findAllByCursor(Long cursor, Long size) {
        return qBookRepository.findAllByCursor(cursor, size).stream().map(BookResponseDto::new).toList();
    }

    // 책 수정
    public BookResponseDto update(Long id, BookUpdateRequestDto requestDto) {
        Book findBook = getBookById(id);

        findBook.updatePost(requestDto);
        return new BookResponseDto(findBook);
    }

    // 책 삭제
    public LocalDateTime softDel(Long id) {
        Book findBook = getBookById(id);
        findBook.delete();
        return new Book().getDeletedAt();
    }


    // 책 전체 top 10
    public List<BookResponseDto> getTopBooks() {
        return qBookRepository.findTop10Books()
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }

    // 책 카테고리별 top 10
    public List<BookResponseDto> getTopBookByCategory(String category) {
        CategoryEnum categoryEnum = CategoryEnum.of(category).orElseThrow(()
                -> new InvalidSearchConditionException(HttpStatus.BAD_REQUEST, "검색 조건이 올바르지 않습니다: " + category)
        );

        return qBookRepository.findTop10BooksByCategory(categoryEnum)
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }

    // 책 나이대 별 top 10
    public List<BookResponseDto> getTopBookByUserAge(String ageGroup) {
        AgeGroup ageGroupEnum = AgeGroup.of(ageGroup).orElseThrow(()
                -> new InvalidSearchConditionException(HttpStatus.BAD_REQUEST, "검색 조건이 올바르지 않습니다: " + ageGroup)
        );

        return qBookRepository.findTop10BooksByAgeGroup(ageGroupEnum)
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }


    /* 도메인 관련 메서드 */

    // 중복되는거 메서드로 뺌 (id로 책찾는거)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,
                        "해당 id로 책을 찾을 수 없습니다. 다른 id를 입력해주세요!"));
    }


}
