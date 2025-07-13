package com.example.book_api.domain.user.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {

    private String password;
    private String newPassword;

}
