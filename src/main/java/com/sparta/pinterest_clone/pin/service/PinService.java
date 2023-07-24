package com.sparta.pinterest_clone.pin.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.pinterest_clone.pin.PinRepository.PinRepository;
import com.sparta.pinterest_clone.pin.dto.PinRequestDto;
import com.sparta.pinterest_clone.pin.dto.PinResponseDto;
import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.pin.entity.PinImage;
import com.sparta.pinterest_clone.security.UserDetailsImpl;
import com.sparta.pinterest_clone.user.entity.User;
import com.sparta.pinterest_clone.user.repository.UserRepository;
import com.sparta.pinterest_clone.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "pin service")
@Service
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;
    private final String bucket;
    private final ImageUtil imageUtil;


    public List<PinResponseDto> getAllPins() {
        List<Pin> pinlist = pinRepository.findAllByOrderByCreatedAtDesc();
        List<PinResponseDto> pinResponseDtoList = new ArrayList<>();
        for (Pin pin : pinlist
        ) {
            pinResponseDtoList.add(new PinResponseDto(pin.getId(), pin.getImage().getImage()));
        }
        return pinResponseDtoList;
    }


    public PinResponseDto getPin(Long pinId) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        return new PinResponseDto(pin);
    }

    @Transactional
    public ResponseEntity<String> updatePin(Long pinId, PinRequestDto pinRequestDto, UserDetailsImpl userDetails) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        User user = userRepository.findById(pin.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
        if (checkAuthority(user, userDetails)) {
            pin.update(pinRequestDto);
            return new ResponseEntity("핀 수정 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity("핀 수정 실패", HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional
    public ResponseEntity<String> deletePin(Long pinId, UserDetailsImpl userDetails) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        User user = userRepository.findById(pin.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
        if (checkAuthority(user, userDetails)) {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, pin.getImage().getImageKey());
            amazonS3.deleteObject(deleteObjectRequest);
            pinRepository.delete(pin);
            return new ResponseEntity("핀 삭제 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity("핀 삭제 실패", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> createPin(PinRequestDto pinRequestDto
                                            , MultipartFile image,
                                            UserDetailsImpl userDetails) {
//        User
        User user = userDetails.getUser();
        //파일 정보
        MultipartFile file = image;
        //파일 검증
        if(!imageUtil.validateFile(file)){throw new IllegalArgumentException("파일 검증 실패");}
        //S3에 업로드 후 이미지 키 반환.
        String fileUuid = imageUtil.uploadFileToS3(file, amazonS3,bucket);

        //핀 이미지 생성.
        PinImage S3ObjectUrl = new PinImage(fileUuid, amazonS3.getUrl(bucket, fileUuid).toString());
        Pin pin = new Pin(pinRequestDto, user, S3ObjectUrl);
        pinRepository.save(pin);
        return ResponseEntity.ok("핀 등록 완료.");
    }

    private boolean checkAuthority(User user, UserDetailsImpl userDetails) {
        if (user.getUserId() == userDetails.getUser().getUserId()) {
            return true;
        } else {
            return false;
        }
    }
}
