package com.sparta.pinterest_clone.pin.dto;

import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.user.entity.User;
import lombok.Getter;

@Getter
public class PinResponseDto {
    Long pin_id;
    String title;
    String content;
    String imageUrl;
    String nickname;
    String userImageUrl;
//    List<Comment> commentList;

    public PinResponseDto(Pin pin){
        this.pin_id=pin.getId();
        this.imageUrl = pin.getImage().getImage();
        this.title = pin.getTitle();
        this.content=pin.getContent();
        this.nickname= pin.getUser().getNickname();
//        this.userImageUrl = user.getUserImage()....
    }
    public PinResponseDto(Long id , String imageUrl){
        this.pin_id=id;
        this.imageUrl = imageUrl;
    }

}
