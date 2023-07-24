package com.sparta.pinterest_clone.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "Global exception")
@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException 처리
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponseDto> handleException(IllegalArgumentException ex) {
        ErrorResponseDto restApiException = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(restApiException);
    }

    // MethodArgumentNotValidException (requestDto에서 valid 관련해서 생기는 예외) 처리
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> handleException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getFieldErrors().forEach((e) -> {
            sb.append(e.getDefaultMessage()).append(" / ");
        });
        sb.setLength(sb.length()-3);
        ErrorResponseDto restApiException = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                sb.toString()
        );
        return ResponseEntity.badRequest().body(restApiException);
    }
}
