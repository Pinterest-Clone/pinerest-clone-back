package com.sparta.pinterest_clone.pin.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.sparta.pinterest_clone.comment.dto.CommentResponseDto;
import com.sparta.pinterest_clone.StatusResponseDto;
import com.sparta.pinterest_clone.comment.entity.Comment;
import com.sparta.pinterest_clone.comment.repository.CommentRepository;
import com.sparta.pinterest_clone.exception.CustomException;
import com.sparta.pinterest_clone.image.Image;
import com.sparta.pinterest_clone.pin.PinRepository.PinLikeRepository;
import com.sparta.pinterest_clone.pin.PinRepository.PinRepository;
import com.sparta.pinterest_clone.pin.dto.PinRequestDto;
import com.sparta.pinterest_clone.pin.dto.PinResponseDto;
import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.pin.entity.PinLike;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j(topic = "pin service")
@Service
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PinLikeRepository pinLikeRepository;
    private final AmazonS3 amazonS3;
    private final String bucket;
    private final ImageUtil imageUtil;
    private ConcurrentMap<String, String> imageUrlCache = new ConcurrentHashMap<>();


    public List<PinResponseDto> getAllPins() {
        List<Pin> pinlist = pinRepository.findAllByOrderByCreatedAtDesc();
        List<PinResponseDto> pinResponseDtoList = new ArrayList<>();
        for (Pin pin : pinlist
        ) {
            pinResponseDtoList.add(new PinResponseDto(pin.getId(), pin.getImage().getImage()));
        }
        return pinResponseDtoList;
    }

    public List<PinResponseDto> searchPin(String keyword) {
        List<Pin> pinlist = pinRepository.searchPinsByKeywordWithPriority(keyword);
        List<PinResponseDto> pinResponseDtoList = new ArrayList<>();
        for (Pin pin : pinlist
        ) {
            pinResponseDtoList.add(new PinResponseDto(pin.getId(), pin.getImage().getImage()));
        }
        return pinResponseDtoList;
    }


    public PinResponseDto getPin(Long pinId) {
        Pin pin = findPin(pinId);

        List<Comment> comments = commentRepository.findAllByPinId(pinId);


        Map<Long, List<Comment>> commentsByParentId = comments.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(Comment::getParentId));

        List<CommentResponseDto> commentList = comments.stream()
                .filter(c -> c.getParentId() == null)
                .map(comment -> {
                    Long parentId = comment.getCommentId();
                    List<CommentResponseDto> subComments =
                            commentsByParentId.getOrDefault(parentId, new ArrayList<>())
                                    .stream()
                                    .map(CommentResponseDto::new)
                                    .collect(Collectors.toList());
                    return new CommentResponseDto(comment, subComments);
                })
                .collect(Collectors.toList());

        return new PinResponseDto(pin, commentList);
    }

    @Transactional
    public StatusResponseDto updatePin(Long pinId, PinRequestDto pinRequestDto, UserDetailsImpl userDetails) {
        Pin pin = findPin(pinId);
        User user = findUser(userDetails.getUser().getUserId());
        checkAuthority(user, userDetails);
        pin.update(pinRequestDto);
        return new StatusResponseDto(HttpStatus.OK, "핀 수정 성공");
    }


    @Transactional
    public StatusResponseDto deletePin(Long pinId, UserDetailsImpl userDetails) {
        Pin pin = findPin(pinId);
        User user = findUser(userDetails.getUser().getUserId());
        checkAuthority(user, userDetails);
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, pin.getImage().getImageKey());
        amazonS3.deleteObject(deleteObjectRequest);
        pinRepository.delete(pin);

        return new StatusResponseDto(HttpStatus.OK, "핀 삭제 성공");
    }


    public StatusResponseDto createPin(PinRequestDto pinRequestDto,
                                                       MultipartFile file,
                                                       UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        //파일 정보
        MultipartFile file = image;
        //파일 검증
        if (!imageUtil.validateFile(file)) {
            throw new CustomException(HttpStatus.NOT_FOUND, "파일 검증 실패");
        }
        //S3에 업로드 후 이미지 키 반환.
        String fileUuid = imageUtil.uploadFileToS3(file, amazonS3, bucket);

        //핀 이미지 생성.
        Image S3ObjectUrl = new Image(fileUuid, amazonS3.getUrl(bucket, fileUuid).toString());
        Pin pin = new Pin(pinRequestDto, user, S3ObjectUrl);
        pinRepository.save(pin);
        return new StatusResponseDto(HttpStatus.OK, "핀 등록 성공");
    }

    @Transactional
    public StatusResponseDto likePin(Long pinId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new IllegalArgumentException("핀이 없습니다."));
        PinLike pinLike = pinLikeRepository.findByUserAndPin(user, pin).orElse(null);
        if (pinLike == null) {
            PinLike newPinLike = new PinLike(user, pin);
            pinLikeRepository.save(newPinLike);
            return new StatusResponseDto(HttpStatus.OK, "좋아요 성공");
        } else {
            pinLikeRepository.delete(pinLike);
            return new StatusResponseDto(HttpStatus.OK, "좋아요 취소");
        }
    }

    public StatusResponseDto savePin(Long pinId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new IllegalArgumentException("핀이 없습니다."));
        PinLike pinLike = pinLikeRepository.findByUserAndPin(user, pin).orElse(null);
        if (pinLike == null) {
            PinLike newPinLike = new PinLike(user, pin);
            pinLikeRepository.save(newPinLike);
            return new StatusResponseDto(HttpStatus.OK, "저장 성공");
        } else {
            pinLikeRepository.delete(pinLike);
            return new StatusResponseDto(HttpStatus.OK, "저장 취소");
        }
    }

    public Pin findPin(Long pinId) {
        return pinRepository.findById(pinId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "게시글이 없습니다."));
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원이 없습니다."));

    }

    private void checkAuthority(User user, UserDetailsImpl userDetails) {
        if (!userDetails.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "작성자만 삭제/수정할 수 있습니다.");
        }
    }
}
