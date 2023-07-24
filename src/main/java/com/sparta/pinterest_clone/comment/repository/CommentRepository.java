package com.sparta.pinterest_clone.comment.repository;

import com.sparta.pinterest_clone.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPinId(Long pinId);
}
