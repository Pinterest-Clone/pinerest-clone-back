package com.sparta.pinterest_clone.exception;


import lombok.Getter;

@Getter
public class ExceptionResponseDto {
    private String message;

    public ExceptionResponseDto(String message) {
        this.message = message;
    }
}
