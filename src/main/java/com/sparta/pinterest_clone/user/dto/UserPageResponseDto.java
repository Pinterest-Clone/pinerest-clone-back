package com.sparta.pinterest_clone.user.dto;

import com.sparta.pinterest_clone.pin.dto.PinResponseDto;
import com.sparta.pinterest_clone.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPageResponseDto {
    private String firstName;
    private String lastName;
    private String nickname;
    private String image;
    private List<PinResponseDto> createdPin = new ArrayList<>();

    private boolean isMyPage;

    private UserPageResponseDto(User user, List<PinResponseDto> pinResponseDtoList, boolean isMyPage) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.nickname = user.getNickname();
        if(user.getImage() == null) {
            this.image = "No Image";
        }
        else {
            this.image = user.getImage().getImage();
        }
        this.createdPin = pinResponseDtoList;
        this.isMyPage = isMyPage;
    }

    public static UserPageResponseDto of(User user, List<PinResponseDto> pinResponseDtoList, boolean isMyPage) {

        return new UserPageResponseDto(user, pinResponseDtoList, isMyPage);
    }
}
