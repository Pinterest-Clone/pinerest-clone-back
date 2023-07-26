package com.sparta.pinterest_clone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
public class StatusResponseDto {
    private String message;
    private HttpStatusCode httpStatusCode;

    public StatusResponseDto(HttpStatusCode httpStatusCode, String message) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
