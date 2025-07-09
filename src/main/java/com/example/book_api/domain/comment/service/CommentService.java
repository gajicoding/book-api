package com.example.book_api.domain.comment.service;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.exception.NotFoundException;
import com.example.book_api.domain.book.service.BookService;
import com.example.book_api.domain.comment.dto.CommentRequestDto;
import com.example.book_api.domain.comment.dto.CommentResponseDto;
import com.example.book_api.domain.comment.entity.Comment;
import com.example.book_api.domain.comment.exception.CommentException;
import com.example.book_api.domain.comment.repository.CommentRepository;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BookService bookService;
    // TODO 서비스로 변경하기
    private final UserRepository userRepository;

    public CommentResponseDto create(Long userId, Long bookId, CommentRequestDto request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("해당 유저는 존재하지 않습니다."));
        Book book = bookService.getBookById(bookId);

        Comment comment = new Comment(request.getContent(), user, book);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment.getId(), savedComment.getContent(), savedComment.getUser().getId(), savedComment.getBook().getId());
    }

    public CommentResponseDto update(Long userId, Long id, CommentRequestDto request) {

        Comment comment = getCommentById(id);
        if(!comment.getUser().getId().equals(userId)) {
            throw new CommentException("수정 할 권한이 없습니다. (작성자와 유저 불일치)");
        }
        comment.changeContent(request.getContent());
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment.getId(), savedComment.getContent(), savedComment.getUser().getId(), savedComment.getBook().getId());
    }

    public LocalDateTime delete(Long userId, Long id) {

        Comment comment = getCommentById(id);
        if(!comment.getUser().getId().equals(userId)) {
            throw new CommentException("삭제 할 권한이 없습니다. (작성자와 유저 불일치)");
        }

        comment.delete(); // soft delete
        commentRepository.save(comment);
        return comment.getDeletedAt();
    }

    // 중복되는 코드 - 메서드로 분리
    public Comment getCommentById(Long id) {
        return commentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new NotFoundException(HttpStatus.NOT_FOUND,
                        "해당 commentId를 찾을 수 없습니다."));
    }

    // 페이징 처리
    public Page<CommentResponseDto> getCommentWithPaging(Long bookId, int page, int size) {
        // bookId가 존재하는지 확인
        Book book = bookService.getBookById(bookId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> paged = commentRepository.findAllByBookIdAndDeletedAtIsNull(bookId, pageable);
        return paged.map(this::toCommentDto);
    }

    public Page<CommentResponseDto> getMyCommentWithPaging(Long userId, int page, int size) {
        // TODO userId가 존재하는지 확인

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> paged = commentRepository.findAllByUserIdAndDeletedAtIsNull(userId, pageable);
        return paged.map(this::toCommentDto);
    }

    private CommentResponseDto toCommentDto(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getContent(), comment.getUser().getId(), comment.getBook().getId());
    }
}
