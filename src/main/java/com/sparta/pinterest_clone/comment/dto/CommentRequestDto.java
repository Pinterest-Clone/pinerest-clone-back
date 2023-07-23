package com.sparta.pinterest_clone.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotBlank
    String comment;

    public CommentRequestDto(String comment) {
        this.comment = comment;
    }
}
