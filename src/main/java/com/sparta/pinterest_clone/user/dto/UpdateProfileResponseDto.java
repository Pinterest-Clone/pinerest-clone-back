package com.sparta.pinterest_clone.user.dto;

import lombok.Data;

@Data
public class UpdateProfileResponseDto {
    private String firstname;

    private String lastname;

    private String introduce;

    private String url;
    private String username;

    public UpdateProfileResponseDto(String firstname, String lastname, String introduce, String url, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.introduce = introduce;
        this.url = url;
        this.username = username;
    }
}
