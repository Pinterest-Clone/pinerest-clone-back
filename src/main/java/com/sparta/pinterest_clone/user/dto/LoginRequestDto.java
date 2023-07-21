package com.sparta.pinterest_clone.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    private String email;

    private String password;

    private String birthday;
}