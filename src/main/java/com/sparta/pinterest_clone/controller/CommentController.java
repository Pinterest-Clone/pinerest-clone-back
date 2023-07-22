package com.sparta.pinterest_clone.controller;

import com.sparta.pinterest_clone.dto.CommentRequestDto;
import com.sparta.pinterest_clone.dto.CommentResponseDto;
import com.sparta.pinterest_clone.dto.StatusResponseDto;
import com.sparta.pinterest_clone.entity.User;
import com.sparta.pinterest_clone.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pin/{pinId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long pinId,
                                                            @RequestBody @Valid CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return new ResponseEntity<>(commentService.createComment(pinId, requestDto, user), HttpStatus.OK);
    }

    // 대댓글 작성
    @PostMapping("comments/{commentId}/replies")
    public ResponseEntity<CommentResponseDto> createSubComment(@PathVariable Long pinId,
                                                               @PathVariable Long commentId,
                                                               @RequestBody @Valid CommentRequestDto requestDto,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return new ResponseEntity<>(commentService.createSubComment(pinId, commentId, requestDto, user), HttpStatus.OK);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                            @RequestBody @Valid CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return new ResponseEntity<>(commentService.updateComment(commentId, requestDto, user), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<StatusResponseDto> deleteComment(@PathVariable Long commentId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return new ResponseEntity<>(commentService.deleteComment(commentId, user), HttpStatus.OK);
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<StatusResponseDto> commentLike(@PathVariable Long commentId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        StatusResponseDto statusCodesResponseDto = commentService.commentLike(commentId, user);
        return new ResponseEntity<>(statusCodesResponseDto, HttpStatus.OK);
    }


}
