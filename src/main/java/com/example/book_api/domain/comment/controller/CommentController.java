package com.example.book_api.domain.comment.controller;

import com.example.book_api.domain.auth.annotation.Auth;
import com.example.book_api.domain.auth.dto.AuthUser;
import com.example.book_api.domain.comment.dto.CommentRequestDto;
import com.example.book_api.domain.comment.dto.CommentResponseDto;
import com.example.book_api.domain.comment.service.CommentService;
import com.example.book_api.domain.log.enums.ActivityType;
import com.example.book_api.global.annotation.Logging;
import com.example.book_api.global.dto.ApiResponse;
import com.example.book_api.global.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // create
    @Logging(activityType = ActivityType.COMMENT_CREATED)
    @PostMapping("/books/{bookId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto>> create(
            @PathVariable Long bookId,
            @RequestBody @Validated CommentRequestDto request,
            @Auth AuthUser authUser
            ) {
        CommentResponseDto response = commentService.create(authUser.getId(), bookId, request);
        return ApiResponse.success(HttpStatus.CREATED, "댓글이 생성되었습니다.", response);
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

        return ApiResponse.success(HttpStatus.OK, "책 댓글이 조회되었습니다.", responses);
    }

    // read : 내가 쓴 댓글 조회
    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<ApiResponse<PagedResponse<CommentResponseDto>>> getMyComments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Auth AuthUser authUser
    ) {
        // 페이징
        Page<CommentResponseDto> paged = commentService.getMyCommentWithPaging(authUser.getId(), userId, page, size);
        PagedResponse<CommentResponseDto> responses = PagedResponse.toPagedResponse(paged);

        return ApiResponse.success(HttpStatus.OK, "댓글이 조회되었습니다", responses);
    }


    // update
    @Logging(activityType = ActivityType.COMMENT_UPDATED)
    @PatchMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> update(
            @PathVariable Long id,
            @RequestBody CommentRequestDto request,
            @Auth AuthUser authUser
    ) {
        CommentResponseDto response = commentService.update(authUser.getId(), id, request);

        return ApiResponse.success(HttpStatus.OK, "댓글이 수정되었습니다.", response);
    }

    // delete
    @Logging(activityType = ActivityType.COMMENT_DELETED)
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<LocalDateTime>> delete(
            @PathVariable Long id,
            @Auth AuthUser authUser
    ) {
        LocalDateTime deletedAt = commentService.delete(authUser.getId(), id);
        return ApiResponse.success(HttpStatus.OK, "댓글이 삭제되었습니다.", deletedAt);
    }
}
