package com.sparta.pinterest_clone.pin.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.pinterest_clone.comment.dto.CommentResponseDto;
import com.sparta.pinterest_clone.comment.entity.Comment;
import com.sparta.pinterest_clone.comment.repository.CommentRepository;
import com.sparta.pinterest_clone.pin.PinRepository.PinRepository;
import com.sparta.pinterest_clone.pin.dto.PinRequestDto;
import com.sparta.pinterest_clone.pin.dto.PinResponseDto;
import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.pin.entity.PinImage;
import com.sparta.pinterest_clone.security.UserDetailsImpl;
import com.sparta.pinterest_clone.user.entity.User;
import com.sparta.pinterest_clone.user.repository.UserRepository;
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
import java.util.stream.Collectors;

@Slf4j(topic = "pin service")
@Service
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3 amazonS3;
    private final String bucket;


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
        List<Comment> comments = commentRepository.findAllByPinId(pinId);
        List<CommentResponseDto> commentList = comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        return new PinResponseDto(pin, commentList);
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
            pinRepository.delete(pin);
//            //pin에 연관된 이미지를 버킷에서 전부 삭제
//            for (String key : pin.getImage().keySet()) {
//                DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
//                amazonS3.deleteObject(deleteObjectRequest);
//            }
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, pin.getImage().getImageKey());
            amazonS3.deleteObject(deleteObjectRequest);
            return new ResponseEntity("핀 삭제 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity("핀 삭제 실패", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> createPin(PinRequestDto pinRequestDto,
                                            UserDetailsImpl userDetails) {

//        User
        User user = userDetails.getUser();

        //파일 정보
        MultipartFile file = pinRequestDto.getImage();
        List<String> fileExtensions = Arrays.asList("jpg", "png", "webp", "heif", "heic", "gif", "jpeg");
        String path = Paths.get(file.getOriginalFilename()).toString(); // 원본 파일명으로 파일 경로 생성
        String extension = StringUtils.getFilenameExtension(path); // 확장자명
        long maxSize = 20 * 1024 * 1024; // 20MB
        long fileSize = file.getSize();

        // 파일 검증
        if (file == null || file.isEmpty() ||
                !fileExtensions.contains(extension.toLowerCase()) ||
                extension == null ||
                fileSize > maxSize) {
            throw new IllegalArgumentException("파일이 적절하지 않습니다.");
        }

        //S3에 image 저장 , 이미지 파일 url을 ...
        String randomUuid = UUID.randomUUID().toString(); // randomUuid 생성.
        String fileUuid = randomUuid + "." + extension;  // randomUuid를 사용해 파일 고유의 id 생성. image의 키로 사용.

        // 업로드할 파일의 메타데이터 생성(확장자 / 파일 크기.byte)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(file.getSize());

        //요청 객체
        PutObjectRequest request;
        try {
            request = new PutObjectRequest(bucket, fileUuid, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //S3 bucket에 put.(등록)
        amazonS3.putObject(request);

//        //S3 bucket에 저장된 이미지의 키 값과 url을 Map자료구조에 담는다.
//        Map<String, String> S3ObjectUrl = new LinkedHashMap<>();
//        S3ObjectUrl.put(fileUuid, amazonS3.getUrl(bucket, fileUuid).toString());

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
