package com.sparta.pinterest_clone.pin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PinImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageKey;

    @Column(nullable = false)
    private String image;

    public PinImage(String imageKey, String image) {
        this.imageKey = imageKey;
        this.image = image;
    }
}
