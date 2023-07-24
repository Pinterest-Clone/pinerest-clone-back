package com.sparta.pinterest_clone.user.dto;

import com.sparta.pinterest_clone.user.entity.User;
import lombok.Data;

@Data
public class UpdateProfileResponseDto {
    private String firstname;

    private String lastname;

    private String introduction;

    private String myurl;

    private String nickname;


    public UpdateProfileResponseDto(User user) {
        this.firstname = user.getFirstName();
        this.lastname = user.getLastName();
        this.introduction = user.getIntroduction();
        this.myurl = user.getMyUrl();
        this.nickname = user.getNickname();

    }
}
