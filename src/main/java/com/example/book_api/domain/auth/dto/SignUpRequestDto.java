package com.example.book_api.domain.auth.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignUpRequestDto {

    private String email;
    private String password;
    private String name;
    private LocalDate birth;

}
