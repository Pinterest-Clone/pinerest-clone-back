package com.sparta.pinterest_clone.pin.controller;

import com.sparta.pinterest_clone.pin.dto.PinRequestDto;
import com.sparta.pinterest_clone.pin.service.PinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PinController {
    private final PinService pinService;

    @PostMapping("/pin/")
    public ResponseEntity<String> createPin(@ModelAttribute PinRequestDto pinRequestDto){
        return pinService.createPin(pinRequestDto);
    }
}
