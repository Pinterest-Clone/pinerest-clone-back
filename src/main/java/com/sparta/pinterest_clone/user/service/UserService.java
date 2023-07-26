package com.sparta.pinterest_clone.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.sparta.pinterest_clone.exception.CustomException;
import com.sparta.pinterest_clone.image.Image;
import com.sparta.pinterest_clone.image.ImageRepository;
import com.sparta.pinterest_clone.security.UserDetailsImpl;
import com.sparta.pinterest_clone.user.dto.LoginRequestDto;
import com.sparta.pinterest_clone.user.dto.UpdateProfileRequestDto;
import com.sparta.pinterest_clone.user.dto.UpdateProfileResponseDto;
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

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
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

    @Transactional
    public UpdateProfileResponseDto updateProfile(UserDetailsImpl userDetails, UpdateProfileRequestDto requestDto, MultipartFile userImage) {

        //파일 정보
        MultipartFile file = userImage;


        if (!imageUtil.validateFile(file)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "파일 검증 실패");
        }

        String fileUuid = imageUtil.uploadFileToS3(file, amazonS3, bucket);

        User user = findUser(userDetails.getUser().getUserId());

        if(user.getImage()!=null){
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, user.getImage().getImageKey());
            amazonS3.deleteObject(deleteObjectRequest);
            imageRepository.delete(user.getImage());
        }

        Image S3ObjectUrl = new Image(fileUuid, amazonS3.getUrl(bucket, fileUuid).toString());

        user.update(requestDto, S3ObjectUrl);
        return new UpdateProfileResponseDto(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.")
        );
    }
}
