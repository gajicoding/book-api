package com.example.book_api.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequestDto {
    @NotBlank(message = "null/공백은 입력 불가합니다.")
    private String email;
    @NotBlank(message = "null/공백은 입력 불가합니다.")
    private String password;

}
