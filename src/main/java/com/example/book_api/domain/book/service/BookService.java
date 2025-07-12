package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookRegistRequestDto;
import com.example.book_api.domain.book.dto.BookTrendResponseDto;
import com.example.book_api.domain.book.dto.BookUpdateRequestDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.entity.BookKeyword;
import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.exception.NotFoundException;
import com.example.book_api.domain.book.repository.BookRepository;
import com.example.book_api.domain.book.repository.QBookRepository;
import com.example.book_api.domain.book.validation.BookValidator;
import com.example.book_api.global.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookViewService bookViewService;
    private final BookKeywordService bookKeywordService;
    private final QBookRepository qBookRepository;
    private final BookValidator bookValidator;

    // 책 등록
    public BookResponseDto regist(BookRegistRequestDto resquestDto) {
        Book newBook = resquestDto.toEntity();
        Book regist = bookRepository.save(newBook);
        return new BookResponseDto(regist);
    }

    // 책 단건 조회
    @Transactional
    public BookResponseDto find(Long id) {
        Book book = getBookById(id);
        bookViewService.viewCount(book, 1L); // 토큰 들어오면 1L 바꾸기
        return new BookResponseDto(book);
    }

    // 책 전체 조회 page, size 방식
    @Transactional
    public PagedResponse<BookResponseDto> findAll(
            int page, int size, String keyword
            ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id");
        Page<Book> books;

        BookKeyword bookKeyword = saveKeyword(keyword);
        if(bookKeyword == null) {
            books = bookRepository.findAll(pageable);
        } else {
            books = qBookRepository.searchAllFields(keyword, pageable);
        }

        return PagedResponse.toPagedResponse(books.map(BookResponseDto::new));
    }

    public BookKeyword saveKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        return bookKeywordService.save(keyword, 1L); // TODO: User Token 값으로 변경 필요
    }

    public boolean isPopularKeyword(String keyword) {
        return qBookRepository.isPopularKeyword(keyword);
    }

    // 책 cursor 방식으로 조회
    public List<BookResponseDto> findAllByCursor(Long cursor, Long size) {
        return qBookRepository.findAllByCursor(cursor, size).stream().map(BookResponseDto::new).toList();
    }

    // 인기 키워드 조회
    public List<BookTrendResponseDto> findKeyword() {
        return qBookRepository.findTrend();
    }


    // 책 수정
    @Caching(evict = {
            @CacheEvict(value = "bookTop", allEntries = true),
            @CacheEvict(value = "books", allEntries = true),
    })
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
        CategoryEnum categoryEnum = bookValidator.validateCategory(category);

        return qBookRepository.findTop10BooksByCategory(categoryEnum)
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }

    // 책 나이대 별 top 10
    public List<BookResponseDto> getTopBookByUserAge(String ageGroup) {
        AgeGroup ageGroupEnum = bookValidator.validateAgeGroup(ageGroup);

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
