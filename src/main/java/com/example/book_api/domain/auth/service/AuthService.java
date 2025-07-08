package com.example.book_api.domain.auth.service;

import com.example.book_api.domain.auth.dto.SignUpRequestDto;
import com.example.book_api.domain.auth.dto.SignUpResponseDto;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.service.UserService;
import com.example.book_api.global.config.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        userService.existsByEmail(signUpRequestDto.getEmail());

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        User newUser = new User(
                signUpRequestDto.getEmail(),
                encodedPassword,
                signUpRequestDto.getName(),
                signUpRequestDto.getBirth()
        );

        User savedUser = userService.saveUser(newUser);

        return new SignUpResponseDto(savedUser.getEmail(), savedUser.getName(), savedUser.getBirth());
    }

}
