package com.sparta.pinterest_clone.pin.dto;

import com.sparta.pinterest_clone.pin.entity.Pin;
import lombok.Getter;

@Getter
public class PinResponseDto {
    Long pin_id;
    String title;
    String content;
    String imageUrl;
    String nickname;
    String userImageUrl;
    private Integer likeCounts;
//    List<Comment> commentList;

    public PinResponseDto(Pin pin) {
        this.pin_id = pin.getId();
        this.imageUrl = pin.getImage().getImage();
        this.title = pin.getTitle();
        this.content = pin.getContent();
        this.nickname = pin.getUser().getNickname();
        this.likeCounts = pin.getPinLikes().size();

        if (pin.getUser().getUserimage() != null) {
            this.userImageUrl = pin.getUser().getUserimage().getImage();
        } else {
            this.userImageUrl = null;
        }
    }

    public PinResponseDto(Long id, String imageUrl) {
        this.pin_id = id;
        this.imageUrl = imageUrl;
    }

}
