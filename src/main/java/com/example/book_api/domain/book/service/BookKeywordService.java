package com.example.book_api.domain.book.service;

import com.example.book_api.domain.book.entity.BookKeyword;
import com.example.book_api.domain.book.repository.BookKeywordRepository;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookKeywordService {

    private final BookKeywordRepository bookKeywordRepository;
    private final UserService userService;



    public void save(String keyword, Long userId) {
        User user = userService.getUserById(userId);
        BookKeyword bookKeyword = new BookKeyword(keyword , user);
        bookKeywordRepository.save(bookKeyword);

    }
}
