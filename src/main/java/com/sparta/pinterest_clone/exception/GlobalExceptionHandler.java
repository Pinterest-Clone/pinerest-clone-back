package com.sparta.pinterest_clone.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "Global exception")
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponseDto> handleCustomException(CustomException ex) {
        log.error("CustomException error: {}", ex.getErrorMessage());
        ExceptionResponseDto responseDto = new ExceptionResponseDto(ex.getErrorMessage());
        ResponseEntity<ExceptionResponseDto> responseEntity = new ResponseEntity<>(responseDto, ex.getHttpStatusCode());
        return responseEntity;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            sb.append(error.getDefaultMessage()).append(" / ");
        });
        sb.setLength(sb.length() - 3);
        String errorMessage = sb.toString();
        return new ResponseEntity<>(new ExceptionResponseDto(errorMessage), ex.getStatusCode());
    }
}
