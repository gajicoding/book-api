package com.example.book_api.domain.book.repository;

import com.example.book_api.domain.book.entity.BookKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookKeywordRepository extends JpaRepository<BookKeyword, Long> {
}
