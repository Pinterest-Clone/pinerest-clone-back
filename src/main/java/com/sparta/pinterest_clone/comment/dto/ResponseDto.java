package com.sparta.pinterest_clone.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class ResponseDto {
    private String message;

    public ResponseDto(String message) {
        this.message = message;
    }
}
