package com.sparta.pinterest_clone.pin.dto;

import com.sparta.pinterest_clone.pin.entity.Pin;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PinResponseDto {
    Long pin_id;
    String title;
    String content;
    String imageUrl;
    String nickname;
    String userImageUrl;

    public PinResponseDto(Pin pin){
        this.pin_id=pin.getId();
        this.title = pin.getTitle();
        this.content = pin.getContent();
        this.nickname = pin.getUser().getNickname();

//        this.imageUrl =pin.getImage().get;
    }
}
