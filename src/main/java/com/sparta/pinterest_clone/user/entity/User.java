package com.sparta.pinterest_clone.user.entity;

import com.sparta.pinterest_clone.image.Image;
import com.sparta.pinterest_clone.pin.entity.PinLike;
import com.sparta.pinterest_clone.user.dto.UpdateProfileRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor // (access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Image image;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PinLike> pinLikes = new ArrayList<>();

    public User(String email, String password, String birthday) {
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.nickname = email.split("@")[0];
        this.lastName = makeLastName(nickname);
    }


    public User(String email, String password, String birthday, String googleId) {
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.nickname = email.split("@")[0];
        this.googleId = googleId;
        this.lastName = makeLastName(nickname);
    }

    public void update(UpdateProfileRequestDto requestDto, Image userimage) {
        this.firstName = requestDto.getFirstname();
        this.lastName = requestDto.getLastname();
        this.introduction = requestDto.getIntroduction();
        this.myUrl = requestDto.getMyUrl();
        this.nickname = requestDto.getNickname();
        this.image = userimage;
    }

    public void setId(long l) {
        this.userId = l;
    }

    public void update(UpdateProfileRequestDto requestDto) {
        this.firstName = requestDto.getFirstname();
        this.lastName = requestDto.getLastname();
        this.introduction = requestDto.getIntroduction();
        this.myUrl = requestDto.getMyUrl();
        this.nickname = requestDto.getNickname();
    }

    private String makeLastName(String nickname) {
        String tmp = nickname.replaceAll("\\d", "");
        return Character.toUpperCase(tmp.charAt(0)) + tmp.substring(1);
    }

}
