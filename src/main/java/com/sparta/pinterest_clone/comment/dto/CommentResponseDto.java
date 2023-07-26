package com.sparta.pinterest_clone.comment.dto;

import com.sparta.pinterest_clone.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private Long parentId;
    private String comment;
    private String nickname;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> subComments;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.parentId = comment.getParentId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();
        this.createdAt = comment.getCreatedAt(); // 추가
    }

    public CommentResponseDto(Comment comment, List<CommentResponseDto> subComments) {
        this.commentId = comment.getCommentId();
        this.parentId = comment.getParentId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.subComments = subComments;
    }
}
