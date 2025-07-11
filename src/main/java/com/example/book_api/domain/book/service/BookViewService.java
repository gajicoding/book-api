package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.entity.BookView;
import com.example.book_api.domain.book.repository.BookViewRepository;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookViewService {
    private final BookViewRepository bookViewRepository;
    private final UserService userService;



    @Transactional
    public void viewCount(Book book, Long userId) {
        User user = userService.findById(userId);
        BookView bookView = new BookView(book, user);
        bookViewRepository.save(bookView);
    }
}