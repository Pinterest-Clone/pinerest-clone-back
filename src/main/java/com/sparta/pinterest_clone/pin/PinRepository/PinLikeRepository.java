package com.sparta.pinterest_clone.pin.PinRepository;

import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.pin.entity.PinLike;
import com.sparta.pinterest_clone.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinLikeRepository extends JpaRepository<PinLike,Long> {
    Optional<PinLike> findByUserAndPin(User user,Pin pin);
}
