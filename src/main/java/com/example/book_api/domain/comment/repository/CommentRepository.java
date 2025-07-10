package com.example.book_api.domain.comment.repository;

import com.example.book_api.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBookIdAndDeletedAtIsNull(Long bookId);
    Optional<Comment> findByIdAndDeletedAtIsNull(Long id);
}
