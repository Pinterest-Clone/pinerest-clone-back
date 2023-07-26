package com.sparta.pinterest_clone.comment.entity;
import com.sparta.pinterest_clone.Timestamped;
import com.sparta.pinterest_clone.comment.dto.CommentRequestDto;
import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String comment;

    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pin pin;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Long parentId;

    @Transient
    private Comment parentComment;

    public Comment(Pin pin, CommentRequestDto requestDto, User user) {
        this.pin = pin;
        this.comment = requestDto.getComment();
        this.nickname = user.getNickname();
        this.user = user;
    }

    public Comment createSubComment(Pin pin, Long parentId, CommentRequestDto requestDto, User user) {
        Comment subComment = new Comment(pin, requestDto, user);
        subComment.setParentId(parentId);
        return subComment;
    }

    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }

}
