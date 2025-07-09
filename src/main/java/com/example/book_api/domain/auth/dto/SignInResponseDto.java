package com.example.book_api.domain.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignInResponseDto {

    private final String token;

}
