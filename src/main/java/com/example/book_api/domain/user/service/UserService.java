package com.example.book_api.domain.user.service;

import com.example.book_api.domain.user.dto.ChangeUserRoleRequestDto;
import com.example.book_api.domain.user.dto.ChangeUserRoleResponseDto;
import com.example.book_api.domain.user.dto.FindUserResponseDto;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.enums.Role;
import com.example.book_api.domain.user.exception.NotFoundUserException;
import com.example.book_api.domain.user.exception.UserException;
import com.example.book_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public FindUserResponseDto findUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundUserException("등록되지 않은 사용자입니다.")
        );
        return new FindUserResponseDto(user.getEmail(), user.getName(), user.getBirth(), user.getRole());
    }

    @Transactional
    public ChangeUserRoleResponseDto changeUserRole(Long id , ChangeUserRoleRequestDto changeUserRoleRequestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundUserException("등록되지 않은 사용자입니다.")
        );
        user.updateRole(Role.of(changeUserRoleRequestDto.getRole()));
        return new ChangeUserRoleResponseDto(user.getEmail(), user.getName(), user.getRole());
    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void existsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException("이미 존재하는 이메일입니다.");
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserException("가입되지 않은 이메일입니다."));
    }

}
