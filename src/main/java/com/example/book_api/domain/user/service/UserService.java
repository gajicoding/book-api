package com.example.book_api.domain.user.service;

import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.exception.UserException;
import com.example.book_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
