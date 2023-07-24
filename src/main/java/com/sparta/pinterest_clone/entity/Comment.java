package com.sparta.pinterest_clone.entity;

import com.sparta.pinterest_clone.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private Long parentId = null;

    private String comment;

    private LocalDateTime createdAt;

    // 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    private Pin pin;

    // user
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Comment(Pin pin, CommentRequestDto requestDto, User user) {
        this.pin = pin;
        this.comment = requestDto.getComment();
        this.user = user;
    }

    public Comment(Pin pin, Long commentId, CommentRequestDto requestDto, User user) {
        this.pin = pin;
        this.parentId = commentId;
        this.comment = requestDto.getComment();
        this.user = user;
    }


    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }

}
