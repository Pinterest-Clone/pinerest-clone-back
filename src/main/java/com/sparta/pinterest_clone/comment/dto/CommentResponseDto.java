package com.sparta.pinterest_clone.comment.dto;

import com.sparta.pinterest_clone.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private Long parentId = null;
    private String comment;
    private String nickname;



    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.parentId = comment.getParentId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();

    }
}
