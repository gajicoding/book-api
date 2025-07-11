package com.example.book_api.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.book_api.domain.user.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class SignInResponseDto {

    @JsonIgnore
    private final Long id;
    private final String email;
    private final String name;
    private final LocalDate birth;
    private final Role role;

}
