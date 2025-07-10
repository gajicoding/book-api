package com.example.book_api.domain.comment.repository;

import com.example.book_api.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndDeletedAtIsNull(Long id);
    Page<Comment> findAllByBookIdAndDeletedAtIsNull(Long id, Pageable pageable);
    Page<Comment> findAllByUserIdAndDeletedAtIsNull(Long id, Pageable pageable);
}
