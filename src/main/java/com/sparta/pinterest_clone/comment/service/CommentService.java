package com.sparta.pinterest_clone.comment.service;

import com.sparta.pinterest_clone.comment.dto.CommentRequestDto;
import com.sparta.pinterest_clone.comment.dto.CommentResponseDto;
import com.sparta.pinterest_clone.comment.dto.ResponseDto;
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
    public CommentResponseDto createComment(Long pinId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        Pin pin = pinRepository.findById(pinId).orElseThrow(()
                -> new EntityNotFoundException("게시글을 찾을 수 없습니다"));

        Comment commentSave = new Comment(pin, requestDto, user);
        Comment comment = commentRepository.save(commentSave);

        return new CommentResponseDto(comment);
    }

    // 대댓글 작성
    @Transactional
    public CommentResponseDto createSubComment(Long pinId, Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new EntityNotFoundException("게시글을 찾을 수 없습니다")
        );

        Comment parentComment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("부모 댓글을 찾을 수 없습니다")
        );

        Comment subComment = parentComment.createSubComment(pin, commentId, requestDto, user);
        Comment saveSubComment = commentRepository.save(subComment);

        return new CommentResponseDto(saveSubComment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Comment comment = findComment(commentId);
        checkAuthority(comment, user);
        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public ResponseDto deleteComment(Long commentId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Comment comment = findComment(commentId);
        checkAuthority(comment, user);

        commentRepository.findAllByPinId(commentId).forEach(commentRepository::delete);
        commentRepository.delete(comment);

        return new ResponseDto("삭제성공");
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new NullPointerException("존재하지 않는 댓글 입니다.")
        );
    }

    public void checkAuthority(Comment comment, User user) {
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new AuthorizationServiceException("작성자만 삭제/수정할 수 있습니다.");
        }
    }
}
