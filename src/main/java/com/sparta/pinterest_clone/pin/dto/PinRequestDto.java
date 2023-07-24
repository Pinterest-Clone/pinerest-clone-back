package com.sparta.pinterest_clone.pin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class PinRequestDto {
    private String title;
    private String content;


    public PinRequestDto(String title, String content) {
        this.title = title;
        this.content = content;

    }
}
