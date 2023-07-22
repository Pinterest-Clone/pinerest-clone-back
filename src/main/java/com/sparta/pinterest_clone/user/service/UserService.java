package com.sparta.pinterest_clone.user.service;

import com.sparta.pinterest_clone.user.dto.LoginRequestDto;
import com.sparta.pinterest_clone.user.entity.User;
import com.sparta.pinterest_clone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void signup(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = passwordEncoder.encode(loginRequestDto.getPassword());
        String birthday = loginRequestDto.getBirthday();

        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = User.of(email, password, birthday);

        userRepository.save(user);
    }
}
