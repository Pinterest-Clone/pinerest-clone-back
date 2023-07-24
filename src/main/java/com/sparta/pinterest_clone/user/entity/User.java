package com.sparta.pinterest_clone.user.entity;

import com.sparta.pinterest_clone.pin.entity.PinImage;
import com.sparta.pinterest_clone.user.dto.UpdateProfileRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Entity @Setter
@NoArgsConstructor // (access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String googleId;
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

    @OneToOne(cascade = CascadeType.ALL)
    private UserImage userimage;

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
        this.nickname = email.split("@")[0];
        this.googleId = googleId;
    }

    public void update(UpdateProfileRequestDto requestDto, UserImage userimage){
        this.firstName = requestDto.getFirstname();
        this.lastName = requestDto.getLastname();
        this.introduction = requestDto.getIntroduction();
        this.myUrl = requestDto.getMyUrl();
        this.nickname = requestDto.getNickname();
        this.userimage = userimage;
    }

    public void setId(long l) {
        this.userId = l;
    }
}
