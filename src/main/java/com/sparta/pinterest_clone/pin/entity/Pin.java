package com.sparta.pinterest_clone.pin.entity;

import com.sparta.pinterest_clone.image.Image;
import com.sparta.pinterest_clone.pin.dto.PinRequestDto;
import com.sparta.pinterest_clone.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Entity //객체?
@Getter
@NoArgsConstructor
public class Pin extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String content;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne
    private User user;

    //commentList

//    @ElementCollection
//    private List<String> tags;

    //pinlike
    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PinLike> pinLikes = new ArrayList<>();


    public Pin(PinRequestDto postRequestDto, User user, Image pinImage) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.image = pinImage;
        this.user = user;
    }

    public void update(PinRequestDto pinRequestDto){
        this.title = pinRequestDto.getTitle();
        this.content = pinRequestDto.getContent();
    }

}
