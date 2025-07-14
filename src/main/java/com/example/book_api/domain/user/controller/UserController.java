package com.example.book_api.domain.user.controller;

import com.example.book_api.domain.auth.dto.AuthUser;
import com.example.book_api.domain.log.enums.ActivityType;
import com.example.book_api.domain.user.dto.*;
import com.example.book_api.domain.user.service.UserService;
import com.example.book_api.global.annotation.Logging;
import com.example.book_api.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @Logging(activityType = ActivityType.USER_ROLE_UPDATED)
    @PatchMapping("/users/{id}")
    public ResponseEntity<ApiResponse<ChangeUserRoleResponseDto>> changeUserRole(
            @PathVariable("id") Long id,
            @RequestBody ChangeUserRoleRequestDto changeUserRoleRequestDto
    ) {
        ChangeUserRoleResponseDto changeUserRoleResponseDto = userService.changeUserRole(id, changeUserRoleRequestDto);
        return ApiResponse.success(HttpStatus.OK, "권한 변경이 완료되었습니다.", changeUserRoleResponseDto);
    }

    @Logging(activityType = ActivityType.USER_PASSWORD_UPDATED)
    @PatchMapping("/users/password")
    public ResponseEntity<ApiResponse<ChangePasswordResponseDto>> changePassword(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody ChangePasswordRequestDto changePasswordRequestDto
    ) {
        ChangePasswordResponseDto changePasswordResponseDto = userService.changePassword(authUser, changePasswordRequestDto);
        return ApiResponse.success(HttpStatus.OK, "비밀번호 변경이 완료되었습니다.", changePasswordResponseDto);
    }

    @Logging(activityType = ActivityType.USER_DELETED)
    @DeleteMapping("/users/withdraw")
    public ResponseEntity<ApiResponse<Object>> deleteUser(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody DeleteRequestDto deleteRequestDto
    ) {
        userService.deleteUser(authUser,deleteRequestDto);
        return ApiResponse.success(HttpStatus.OK, "회원 탈퇴가 완료되었습니다.", null);
    }

}
