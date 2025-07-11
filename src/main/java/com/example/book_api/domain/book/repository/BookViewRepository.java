package com.example.book_api.domain.book.repository;

import com.example.book_api.domain.book.entity.BookView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookViewRepository extends JpaRepository<BookView, Long> {



}
