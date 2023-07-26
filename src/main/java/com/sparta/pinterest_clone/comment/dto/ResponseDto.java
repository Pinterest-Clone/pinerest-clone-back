package com.sparta.pinterest_clone.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResponseDto {
    private String message;

    public ResponseDto(String message) {
        this.message = message;
    }
}
