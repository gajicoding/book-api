package com.example.book_api.domain.user.controller;

import com.example.book_api.domain.user.dto.FindUserResponseDto;
import com.example.book_api.domain.user.service.UserService;
import com.example.book_api.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<FindUserResponseDto>> findUser(@PathVariable ("id") Long id) {
        FindUserResponseDto findUserResponseDto = userService.findUser(id);
        return ApiResponse.success(HttpStatus.OK, "조회를 완료했습니다.", findUserResponseDto);
    }
}
