package com.sparta.pinterest_clone.comment.controller;

import com.sparta.pinterest_clone.comment.dto.CommentRequestDto;
import com.sparta.pinterest_clone.comment.dto.CommentResponseDto;
import com.sparta.pinterest_clone.comment.dto.ResponseDto;
import com.sparta.pinterest_clone.comment.service.CommentService;
import com.sparta.pinterest_clone.security.UserDetailsImpl;
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
    public CommentResponseDto createComment(@PathVariable Long pinId,
                                            @RequestBody @Valid CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return commentService.createComment(pinId, requestDto, userDetails);
    }

    // 대댓글
    @PostMapping("comments/{commentId}/replies")
    public CommentResponseDto createSubComment(@PathVariable Long pinId,
                                               @PathVariable Long commentId,
                                               @RequestBody @Valid CommentRequestDto requestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return commentService.createSubComment(pinId, commentId, requestDto, userDetails);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                            @RequestBody @Valid CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return new ResponseEntity<>(commentService.updateComment(commentId, requestDto, userDetails), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable Long commentId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return new ResponseEntity<>(commentService.deleteComment(commentId, userDetails), HttpStatus.OK);
    }

//    @PostMapping("/comments/{commentId}/like")
//    public ResponseEntity<ResponseDto> commentLike(@PathVariable Long pinId,
//                                                         @PathVariable Long commentId,
//                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        return new ResponseEntity<>(commentService.commentLike(pinId, commentId, userDetails), HttpStatus.OK);
//    }


}
