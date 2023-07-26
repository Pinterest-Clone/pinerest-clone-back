package com.sparta.pinterest_clone.pin.PinRepository;

import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.pin.entity.PinLike;
import com.sparta.pinterest_clone.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PinSaveRepository extends JpaRepository<PinLike,Long> {
    Optional<PinLike> findByUserAndPin(User user,Pin pin);

    @Query("SELECT pl.pin FROM PinSave pl WHERE pl.user = :user")
    List<Pin> findPinsByUser(@Param("user") User user);
}
