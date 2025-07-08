package com.example.book_api.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class SignUpResponseDto {

    private final String email;
    private final String name;
    private final LocalDate birth;

}
