package com.example.book_api.domain.auth.controller;

import com.example.book_api.domain.auth.dto.SignInRequestDto;
import com.example.book_api.domain.auth.dto.SignInResponseDto;
import com.example.book_api.domain.auth.dto.SignUpRequestDto;
import com.example.book_api.domain.auth.dto.SignUpResponseDto;
import com.example.book_api.domain.auth.service.AuthService;
import com.example.book_api.domain.log.enums.ActivityType;
import com.example.book_api.global.annotation.Logging;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.global.config.JwtUtil;
import com.example.book_api.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Logging(activityType = ActivityType.USER_REGISTERED)
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = authService.signUp(signUpRequestDto);
        return ApiResponse.success(HttpStatus.OK, "회원가입이 완료되었습니다.", signUpResponseDto);
    }

    @Logging(activityType = ActivityType.USER_LOG_IN)
    @PostMapping("/auth/signin")
    public ResponseEntity<ApiResponse<SignInResponseDto>> signIn(@RequestBody SignInRequestDto signInRequestDto) {
        SignInResponseDto signInResponseDto = authService.signIn(signInRequestDto);
        User user = authService.findUser(signInResponseDto.getEmail());

        String token = jwtUtil.createToken(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);

        return ApiResponse.success(HttpStatus.OK, "로그인이 완료되었습니다.", signInResponseDto, httpHeaders);
    }

}
