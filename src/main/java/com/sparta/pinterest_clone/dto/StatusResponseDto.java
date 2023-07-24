package com.sparta.pinterest_clone.dto;


public class StatusResponseDto {
    private String message;
    private int statusCode;

    public StatusResponseDto(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
