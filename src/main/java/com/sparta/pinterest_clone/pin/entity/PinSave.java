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
public class PinSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;


    public PinSave(User user, Pin pin) {
        this.user = user;
        this.pin = pin;
    }
}