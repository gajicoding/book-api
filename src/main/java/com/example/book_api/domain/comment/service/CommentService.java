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
import com.example.book_api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BookService bookService;
    private final UserService userService;

    @Transactional
    public CommentResponseDto create(Long userId, Long bookId, CommentRequestDto request) {
        User user = userService.findById(userId);
        Book book = bookService.getBookById(bookId);

        Comment comment = new Comment(request.getContent(), user, book);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment.getId(), savedComment.getContent(), savedComment.getUser().getId(), savedComment.getUser().getName(), savedComment.getBook().getId());
    }

    @Transactional
    public CommentResponseDto update(Long userId, Long id, CommentRequestDto request) {
        Comment comment = getCommentById(id);
        if(!comment.getUser().getId().equals(userId)) {
            throw new CommentException(HttpStatus.UNAUTHORIZED, "수정할 권한이 없습니다. (로그인한 유저와 댓글 작성자 불일치)"); // 권한 없음 에러
        }

        comment.changeContent(request.getContent());

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getBook().getId()
        );
    }

    @Transactional
    public LocalDateTime delete(Long userId, Long id) {
        Comment comment = getCommentById(id);
        if(!comment.getUser().getId().equals(userId)) {
            throw new CommentException(HttpStatus.UNAUTHORIZED, "삭제할 권한이 없습니다. (로그인한 유저와 댓글 작성자 불일치)");
        }

        comment.delete();
        return comment.getDeletedAt();
    }

    @Transactional(readOnly = true)
    // 페이징 처리
    public Page<CommentResponseDto> getCommentWithPaging(Long bookId, int page, int size) {
        // bookId가 존재하는지 검사
        Book book = bookService.getBookById(bookId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> paged = commentRepository.findAllByBookIdAndDeletedAtIsNull(bookId, pageable);
        return paged.map(this::toCommentDto);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getMyCommentWithPaging(Long loginId, Long userId, int page, int size) {
        // 검사
        if(!loginId.equals(userId)) {
            throw new CommentException(HttpStatus.UNAUTHORIZED, "조회할 권한이 없습니다. (로그인한 유저와 댓글 작성자 불일치)");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> paged = commentRepository.findAllByUserIdAndDeletedAtIsNull(userId, pageable);
        return paged.map(this::toCommentDto);
    }

    private CommentResponseDto toCommentDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getName(), // n+1 문제 발생
                comment.getBook().getId()
        );
    }

    // 중복되는 코드 - 메서드로 분리
    public Comment getCommentById(Long id) {
        return commentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new NotFoundException(HttpStatus.NOT_FOUND,
                        "해당 commentId를 찾을 수 없습니다."));
    }

}
