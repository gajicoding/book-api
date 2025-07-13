package com.example.book_api.domain.user.service;

import com.example.book_api.domain.auth.dto.AuthUser;
import com.example.book_api.domain.user.dto.*;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.enums.Role;
import com.example.book_api.domain.user.exception.NotFoundUserException;
import com.example.book_api.domain.user.exception.UserException;
import com.example.book_api.domain.user.repository.UserRepository;
import com.example.book_api.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public ChangePasswordResponseDto changePassword(AuthUser authUser, ChangePasswordRequestDto changePasswordRequestDto) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(
                () -> new NotFoundUserException("사용자를 찾을 수 없습니다.")
        );

        if (!passwordEncoder.matches(changePasswordRequestDto.getPassword(), user.getPassword())) {
            throw new UserException("잘못된 비밀번호입니다.");
        }
        if (passwordEncoder.matches(changePasswordRequestDto.getNewPassword(), user.getPassword())) {
            throw new UserException("현재 비밀번호와 새 비밀번호가 동일합니다.");
        }

        String newPassword = passwordEncoder.encode(changePasswordRequestDto.getNewPassword());

        user.updatePassword(newPassword);

        return new ChangePasswordResponseDto(user.getEmail(), user.getName(), user.getBirth(), user.getRole());
    }

    @Transactional
    public void deleteUser(AuthUser authUser, DeleteRequestDto deleteRequestDto) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(
                () -> new NotFoundUserException("사용자를 찾을 수 없습니다.")
        );

        if (!passwordEncoder.matches(deleteRequestDto.getPassword(), user.getPassword())) {
            throw new UserException("잘못된 비밀번호입니다.");
        }

        user.delete();
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

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                ()-> new NotFoundUserException("해당 id로 유저을 찾을 수 없습니다. 다른 id를 입력해주세요!"));
    }
}
