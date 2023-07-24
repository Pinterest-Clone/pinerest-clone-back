package com.sparta.pinterest_clone.comment.service;

import com.sparta.pinterest_clone.comment.dto.CommentRequestDto;
import com.sparta.pinterest_clone.comment.dto.CommentResponseDto;
import com.sparta.pinterest_clone.comment.dto.StatusResponseDto;
import com.sparta.pinterest_clone.comment.entity.Comment;
import com.sparta.pinterest_clone.comment.repository.CommentRepository;
import com.sparta.pinterest_clone.pin.PinRepository.PinRepository;
import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.security.UserDetailsImpl;
import com.sparta.pinterest_clone.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PinRepository pinRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long pinId, CommentRequestDto requestDto, UserDetailsImpl userDetails){
        User user = userDetails.getUser();

        Pin pin = pinRepository.findById(pinId).orElseThrow(()
                -> new EntityNotFoundException("게시글을 찾을 수 없습니다"));

        Comment commentSave = new Comment(pin, requestDto, user);
        Comment comment = commentRepository.save(commentSave);
        CommentResponseDto responseDto = new CommentResponseDto(comment);

        return responseDto;
    }

    // 대댓글 작성
    @Transactional
    public CommentResponseDto createSubComment(Long pinId, Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails){
        User user = userDetails.getUser();

        Pin pin = pinRepository.findById(pinId).orElseThrow(()
                -> new EntityNotFoundException("게시글을 찾을 수 없습니다"));

        Comment commentSave = new Comment(pin, commentId, requestDto, user);
        Comment comment = commentRepository.save(commentSave);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto,UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        Comment comment = findComment(commentId);
        checkAuthority(comment, user);
        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public StatusResponseDto deleteComment(Long commentId,UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        Comment comment = findComment(commentId);
        checkAuthority(comment, user);
        commentRepository.delete(comment);

        return new StatusResponseDto(200, "삭제성공");
    }


    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new NullPointerException("존재하지 않는 댓글 입니다.")
        );
    }

    public StatusResponseDto commentLike(Long commentId, UserDetailsImpl userDetails) {
        return null;
    }
    // 수정, 삭제시 권한을 확인 .
    public void checkAuthority(Comment comment, User user) {
        // admin 확인
//        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            // 작성자 본인 확인
            if (!comment.getUser().getUserId().equals(user.getUserId())) {
                throw new AuthorizationServiceException("작성자만 삭제/수정할 수 있습니다.");
            }
//        }
    }
}
