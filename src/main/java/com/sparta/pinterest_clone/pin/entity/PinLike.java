package com.sparta.pinterest_clone.pin.entity;

import com.sparta.pinterest_clone.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PinLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Pin pin;


    public PinLike(User user, Pin pin) {
        this.user = user;
        this.pin = pin;
    }
}