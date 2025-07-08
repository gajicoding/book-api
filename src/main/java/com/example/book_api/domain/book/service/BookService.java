package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.dto.BookResponseDto;
import com.example.book_api.domain.book.dto.BookRegistResquestDto;
import com.example.book_api.domain.book.dto.BookUpdateRequestDto;
import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.exception.NotFoundException;
import com.example.book_api.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        Book byId = getBookById(id);
        return new BookResponseDto(byId);
    }


    public Page<BookResponseDto> findAll(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
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

    public BookResponseDto update(Long id, BookUpdateRequestDto requestDto) {
        Book findBook = getBookById(id);

        findBook.updatePost(requestDto);
       return new BookResponseDto(findBook);
    }



    public LocalDateTime softDel(Long id) {
        Book findBook = getBookById(id);
        findBook.delete();
        return new Book().getDeletedAt();
    }


    // 중복되는거 메서드로 뺌 (id로 책찾는거)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(HttpStatus.NOT_FOUND,
                        "해당 id로 책을 찾을 수 없습니다. 다른 id를 입력해주세요!"));
    }
}
