package com.sparta.pinterest_clone.pin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Entity //객체?
@Getter
public class Pin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 100)
    private String tite;

    @Column(nullable = false , length = 255)
    private String content;

    @ElementCollection
    @Column(nullable = false)
    private Map<String,String> pinImage;

    @ElementCollection
    private List<String> tags;

//    @ManyToOne
//    private User user;

    //commentList

    //pinlike
}
