package com.sparta.pinterest_clone.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;

@Slf4j
@Getter
public class CustomException extends RuntimeException {
    private HttpStatusCode httpStatusCode;
    private String errorMessage;

    public CustomException(HttpStatusCode httpStatusCode, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
    }
}

