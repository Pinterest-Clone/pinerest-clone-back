package com.sparta.pinterest_clone.user.repository;

import com.sparta.pinterest_clone.user.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage,Long> {
}
