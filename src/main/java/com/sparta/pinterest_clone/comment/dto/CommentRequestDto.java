package com.sparta.pinterest_clone.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @NotBlank
    String comment;
}
