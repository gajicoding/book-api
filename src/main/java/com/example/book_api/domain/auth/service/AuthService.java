package com.example.book_api.domain.auth.service;

import com.example.book_api.domain.auth.dto.SignInRequestDto;
import com.example.book_api.domain.auth.dto.SignInResponseDto;
import com.example.book_api.domain.auth.dto.SignUpRequestDto;
import com.example.book_api.domain.auth.dto.SignUpResponseDto;
import com.example.book_api.domain.auth.exception.AuthException;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.enums.Role;
import com.example.book_api.domain.user.service.UserService;
import com.example.book_api.global.util.PasswordEncoder;
import com.example.book_api.global.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        userService.existsByEmail(signUpRequestDto.getEmail());

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        User newUser = new User(
                signUpRequestDto.getEmail(),
                encodedPassword,
                signUpRequestDto.getName(),
                signUpRequestDto.getBirth(),
                Role.of(signUpRequestDto.getRole())
        );

        User savedUser = userService.saveUser(newUser);

        return new SignUpResponseDto(savedUser);
    }

    @Transactional
    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        User user = userService.findByEmail(signInRequestDto.getEmail());

        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        return new SignInResponseDto(user);
    }


    public User findUser(String email) {
        return userService.findByEmail(email);
    }

}
