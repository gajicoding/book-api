package com.example.book_api.domain.auth.dto;

import com.example.book_api.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.book_api.domain.user.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class SignInResponseDto {

    @JsonIgnore
    private final Long id;
    private final String email;
    private final String name;
    private final LocalDate birth;
    private final Role role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SignInResponseDto(User user) {
        id = user.getId();
        email = user.getEmail();
        name = user.getName();
        birth = user.getBirth();
        role = user.getRole();
        createdAt = user.getCreatedAt();
        modifiedAt = user.getModifiedAt();
    }
}
