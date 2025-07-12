package com.example.book_api.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class SignUpResponseDto {

    @JsonIgnore
    private final Long id;

    private final String email;
    private final String name;
    private final LocalDate birth;

}
