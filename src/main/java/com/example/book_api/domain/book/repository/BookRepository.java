package com.example.book_api.domain.book.repository;

import com.example.book_api.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = """
    SELECT b.*
    FROM (
        SELECT book_id
        FROM book_views
        GROUP BY book_id
        ORDER BY COUNT(*) DESC
        LIMIT 10
    ) bv_top
    JOIN books b ON b.id = bv_top.book_id
    """, nativeQuery = true)
    List<Book> findTop10Books();
}
