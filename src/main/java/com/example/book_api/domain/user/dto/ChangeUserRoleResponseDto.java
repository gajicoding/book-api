package com.example.book_api.domain.user.dto;

import com.example.book_api.domain.user.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChangeUserRoleResponseDto {

    private final String email;
    private final String name;
    private final Role role;

}
