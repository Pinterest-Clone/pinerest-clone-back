package com.sparta.pinterest_clone.pin.PinRepository;

import com.sparta.pinterest_clone.pin.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {
    List<Pin> findAllByOrderByCreatedAtDesc();
}
