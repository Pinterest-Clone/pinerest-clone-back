//package com.sparta.pinterest_clone.service;
//
//import com.sparta.pinterest_clone.dto.CommentRequestDto;
//import com.sparta.pinterest_clone.dto.CommentResponseDto;
//import com.sparta.pinterest_clone.dto.StatusResponseDto;
//import com.sparta.pinterest_clone.entity.Comment;
//import com.sparta.pinterest_clone.user.entity.User;
//import com.sparta.pinterest_clone.pin.entity.Pin;
//import com.sparta.pinterest_clone.repository.CommentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.AuthorizationServiceException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class CommentService {
//    private final CommentRepository commentRepository;
//
//    // 댓글 작성
//    @Transactional
//    public CommentResponseDto createComment(Long pinId, CommentRequestDto requestDto, User user){
//        Pin pin = PinService.findPin(pinId);
//        Comment commentSave = new Comment(pin, requestDto, user);
//        Comment comment = commentRepository.save(commentSave);
//
//        return new CommentResponseDto(comment);
//    }
//
//    // 대댓글 작성
//    @Transactional
//    public CommentResponseDto createSubComment(Long pinId, Long commentId, CommentRequestDto requestDto, User user) {
//        Pin pin = PinService.findPin(pinId);
//        Comment commentSave = new Comment(pin, commentId, requestDto, user);
//        Comment comment = commentRepository.save(commentSave);
//
//        return new CommentResponseDto(comment);
//    }
//
//    @Transactional
//    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
//        Comment comment = findComment(commentId);
//        checkAuthority(comment, user);
//        comment.update(requestDto);
//
//        return new CommentResponseDto(comment);
//    }
//
//    @Transactional
//    public StatusResponseDto deleteComment(Long commentId, User user) {
//        Comment comment = findComment(commentId);
//        checkAuthority(comment, user);
//        commentRepository.delete(comment);
//
//        return new StatusResponseDto(200, "삭제성공");
//    }
//
//
//    private Comment findComment(Long id) {
//        return commentRepository.findById(id).orElseThrow(() ->
//                new NullPointerException("존재하지 않는 댓글 입니다.")
//        );
//    }
//    // 수정, 삭제시 권한을 확인 .
//    public void checkAuthority(Comment comment, User user) {
//        // admin 확인
//        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
//            // 작성자 본인 확인
//            if (!comment.getUser().getUserId().equals(user.getUserId())) {
//                throw new AuthorizationServiceException("작성자만 삭제/수정할 수 있습니다.");
//            }
//        }
//    }
//}
