package com.example.book_api.domain.comment.controller;

import com.example.book_api.domain.comment.dto.CommentRequestDto;
import com.example.book_api.domain.comment.dto.CommentResponseDto;
import com.example.book_api.domain.comment.service.CommentService;
import com.example.book_api.global.dto.ApiResponse;
import com.example.book_api.global.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
// TODO 기능 구현 모두 완료 후, 요구사항 확인 후 수정 / api문서도 수정
// TODO API, Entity(varchar)와 일치하는지 확인 수정
// TODO Validated 예외처리
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // create
    @PostMapping("/books/{bookId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto>> create(
            @PathVariable Long bookId,
            @RequestBody @Validated CommentRequestDto request
    ) {
        // TODO 인가 처리
        Long userId = 1L;

        CommentResponseDto response = commentService.create(userId, bookId, request);

        return ApiResponse.success(HttpStatus.CREATED, userId + ": 댓글이 생성되었습니다.", response);
    }

    // read : 댓글 목록 조회
    @GetMapping("/books/{bookId}/comments")
    public ResponseEntity<ApiResponse<PagedResponse<CommentResponseDto>>> getComments(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // 페이징
        Page<CommentResponseDto> paged = commentService.getCommentWithPaging(bookId, page, size);
        PagedResponse<CommentResponseDto> responses = PagedResponse.toPagedResponse(paged);

        return ApiResponse.success(HttpStatus.OK, bookId + "번 책 댓글이 조회되었습니다.", responses);
    }

    // read : 내가 쓴 댓글 조회
    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<ApiResponse<PagedResponse<CommentResponseDto>>> getMyComments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // 페이징
        Page<CommentResponseDto> paged = commentService.getMyCommentWithPaging(userId, page, size);
        PagedResponse<CommentResponseDto> responses = PagedResponse.toPagedResponse(paged);

        return ApiResponse.success(HttpStatus.OK, "댓글이 조회되었습니다", responses);
    }


    // update
    @PatchMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> update(
            @PathVariable Long id,
            @RequestBody CommentRequestDto request
    ) {

        // TODO 인가 처리
        Long userId = 1L;

        CommentResponseDto response = commentService.update(userId, id, request);

        return ApiResponse.success(HttpStatus.OK, userId + "댓글이 수정되었습니다.", response);
    }

    // delete
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<LocalDateTime>> delete(
            @PathVariable Long id
    ) {

        // TODO 인가 처리
        Long userId = 1L;

        LocalDateTime deletedAt = commentService.delete(userId, id);
        return ApiResponse.success(HttpStatus.OK, "댓글이 삭제되었습니다.", deletedAt);
    }
}
