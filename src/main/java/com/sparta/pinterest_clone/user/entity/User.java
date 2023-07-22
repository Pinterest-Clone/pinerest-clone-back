package com.sparta.pinterest_clone.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String googleId;
    private String profileImage;
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;
    private String introduction;
    private String myUrl;
    @Column(nullable = false)
    private String birthday;

    public User updateGoogleId(String googleId) {
        this.googleId = googleId;
        return this;
    }

    public User(String email, String password, String birthday) {
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.nickname = email.split("@")[0];
    }

    public User(String email, String password, String birthday, String googleId) {
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.username = email.split("@")[0];
        this.googleId = googleId;
    }
}
