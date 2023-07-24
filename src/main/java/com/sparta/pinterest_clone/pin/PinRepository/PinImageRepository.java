package com.sparta.pinterest_clone.pin.PinRepository;

import com.sparta.pinterest_clone.pin.entity.PinImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinImageRepository extends JpaRepository<PinImage,Long> {
}
