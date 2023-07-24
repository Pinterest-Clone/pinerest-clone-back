package com.sparta.pinterest_clone.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserImage {
    @Id
    private String imageKey;

    @Column(nullable = false)
    private String image;

    public UserImage(String imageKey, String image) {
        this.imageKey = imageKey;
        this.image = image;
    }
}
