package com.sparta.pinterest_clone.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GoogleUserInfo {
    private String googleId;
    private String email;
    private String birthday;

    public GoogleUserInfo(String googleId, String email, String birthday) {
        this.googleId = googleId;
        this.email = email;
        this.birthday = birthday;
    }
}
