package com.sparta.pinterest_clone.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.sparta.pinterest_clone.exception.CustomException;
import com.sparta.pinterest_clone.image.Image;
import com.sparta.pinterest_clone.image.ImageRepository;
import com.sparta.pinterest_clone.pin.PinRepository.PinLikeRepository;
import com.sparta.pinterest_clone.pin.PinRepository.PinRepository;
import com.sparta.pinterest_clone.pin.PinRepository.PinSaveRepository;
import com.sparta.pinterest_clone.pin.dto.PinResponseDto;
import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.security.UserDetailsImpl;
import com.sparta.pinterest_clone.user.dto.LoginRequestDto;
import com.sparta.pinterest_clone.user.dto.UpdateProfileRequestDto;
import com.sparta.pinterest_clone.user.dto.UpdateProfileResponseDto;
import com.sparta.pinterest_clone.user.dto.UserPageResponseDto;
import com.sparta.pinterest_clone.user.entity.User;
import com.sparta.pinterest_clone.user.repository.UserRepository;
import com.sparta.pinterest_clone.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final PinRepository pinRepository;
    private final PinSaveRepository pinSaveRepository;
    private final AmazonS3 amazonS3;
    private final String bucket;
    private final ImageUtil imageUtil;

    public void signup(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = passwordEncoder.encode(loginRequestDto.getPassword());
        String birthday = loginRequestDto.getBirthday();

        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다.");
        }

        User user = new User(email, password, birthday);

        userRepository.save(user);
    }

    public UserPageResponseDto getUserPage(UserDetailsImpl userDetails, String nickname) {
        User user = userCheck(nickname);
        List<Pin> pinList = pinRepository.findByUser(user);
        List<PinResponseDto> pinResponseDtoList = new ArrayList<>();
        for (Pin pin : pinList) {
            pinResponseDtoList.add(new PinResponseDto(pin.getId(), pin.getImage().getImage()));
        }

        boolean isMyPage = false;
        if (userDetails != null) {
            isMyPage = userDetails.getUser().getNickname().equals(nickname);
        }

        return UserPageResponseDto.of(user, pinResponseDtoList, isMyPage);
    }

    @Transactional
    public UpdateProfileResponseDto updateProfile(UserDetailsImpl userDetails, UpdateProfileRequestDto requestDto, MultipartFile file) {
        User user = userCheck(userDetails.getUser().getUserId());
        if(file != null) {
            imageUtil.validateFile(file);

            String fileUuid = imageUtil.uploadFileToS3(file, amazonS3, bucket);

            if (user.getImage() != null) {
                DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, user.getImage().getImageKey());
                amazonS3.deleteObject(deleteObjectRequest);
                imageRepository.delete(user.getImage());
            }

            Image S3ObjectUrl = new Image(fileUuid, amazonS3.getUrl(bucket, fileUuid).toString());

            user.update(requestDto, S3ObjectUrl);
        }
        else {
            user.update(requestDto);
        }
        return new UpdateProfileResponseDto(user);
    }

    public List<PinResponseDto> getPinsByUser(String nickname, boolean wantSavedPin) {
        User user = userCheck(nickname);
        List<Pin> pinList = wantSavedPin ? pinSaveRepository.findPinsByUser(user) : pinRepository.findByUser(user);
        List<PinResponseDto> pinResponseDtoList = new ArrayList<>();
        for (Pin pin : pinList) {
            pinResponseDtoList.add(new PinResponseDto(pin.getId(), pin.getImage().getImage()));
        }

        return pinResponseDtoList;
    }

    public User userCheck(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));
    }

    private User userCheck(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.")
        );
    }
}
