package com.sparta.pinterest_clone.comment.dto;

import com.sparta.pinterest_clone.comment.entity.Comment;

import java.util.List;

public class CommentResponseDto {
    private Long commentId;
    private Long parentId = null;
    private String comment;
    private String nickname;

    private List<String> LikesList; // 형태 바꿔서 줄 생각

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.parentId = comment.getParentId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();
    }
}
