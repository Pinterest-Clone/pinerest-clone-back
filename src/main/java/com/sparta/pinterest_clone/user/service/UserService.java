package com.sparta.pinterest_clone.user.service;

import com.sparta.pinterest_clone.comment.dto.CommentResponseDto;
import com.sparta.pinterest_clone.security.UserDetailsImpl;
import com.sparta.pinterest_clone.user.dto.LoginRequestDto;
import com.sparta.pinterest_clone.user.dto.UpdateProfileRequestDto;
import com.sparta.pinterest_clone.user.dto.UpdateProfileResponseDto;
import com.sparta.pinterest_clone.user.entity.User;
import com.sparta.pinterest_clone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
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

    @Transactional
    public UpdateProfileResponseDto updateProfile(UserDetailsImpl userDetails, UpdateProfileRequestDto requestDto) {
        User user = findUser(userDetails.getUser().getUserId());
        user.update(requestDto);

       return new UpdateProfileResponseDto(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NullPointerException("존재하지 않는 사용자입니다.")
        );
    }
}
