package com.sparta.pinterest_clone.repository;

import com.sparta.pinterest_clone.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
