package com.sparta.pinterest_clone.exception;

public class ErrorResponseDto {
    private int errorCode;
    private String errorMessage;

    public ErrorResponseDto(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters and setters (생략)
}