package com.sparta.pinterest_clone.pin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.pinterest_clone.comment.dto.CommentResponseDto;
import com.sparta.pinterest_clone.comment.entity.Comment;
import com.sparta.pinterest_clone.pin.entity.Pin;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PinResponseDto {
    Long pin_id;
    String title;
    String imageUrl;
    String nickname;
    String userImageUrl;
    List<CommentResponseDto> comments;
    private Integer likeCounts;



    public PinResponseDto(Pin pin, List<CommentResponseDto> comments) {
        this.pin_id = pin.getId();
        this.imageUrl = pin.getImage().getImage();
        this.title = pin.getTitle();
        this.content = pin.getContent();
        this.nickname = pin.getUser().getNickname();
        this.comments = comments;
        this.likeCounts = pin.getPinLikes().size();

        if (pin.getUser().getImage() != null) {
            this.userImageUrl = pin.getUser().getImage().getImage();
        } else {
            this.userImageUrl = null;
        }
    }

    String content;
    public PinResponseDto(Long id, String imageUrl) {
        this.pin_id = id;
        this.imageUrl = imageUrl;
    }

}
