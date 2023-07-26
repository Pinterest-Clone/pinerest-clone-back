package com.sparta.pinterest_clone.comment.repository;

import com.sparta.pinterest_clone.comment.entity.Comment;
import com.sparta.pinterest_clone.comment.entity.CommentLike;
import com.sparta.pinterest_clone.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike findByCommentAndUser(Comment comment, User user);

}
