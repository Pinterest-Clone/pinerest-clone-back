package com.sparta.pinterest_clone.image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image {
    @Id
    private String imageKey;

    @Column(nullable = false)
    private String image;

    public Image(String imageKey, String image) {
        this.imageKey = imageKey;
        this.image = image;
    }
}
