package com.example.book_api.domain.user.dto;

import com.example.book_api.domain.user.enums.Role;
import lombok.Getter;

@Getter
public class ChangeUserRoleRequestDto {

    private String role;

}
