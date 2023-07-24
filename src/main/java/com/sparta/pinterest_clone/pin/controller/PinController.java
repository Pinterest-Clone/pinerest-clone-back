package com.sparta.pinterest_clone.pin.controller;

import com.sparta.pinterest_clone.pin.dto.PinRequestDto;
import com.sparta.pinterest_clone.pin.dto.PinResponseDto;
import com.sparta.pinterest_clone.pin.service.PinService;
import com.sparta.pinterest_clone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PinController {
    private final PinService pinService;

    @GetMapping("/pin")
    public List<PinResponseDto> getAllPins() {
        return pinService.getAllPins();
    }

    @GetMapping("/pin/{pinId}")
    public PinResponseDto getPin(@PathVariable Long pinId) {
        return pinService.getPin(pinId);
    }

    @PutMapping("/pin/{pinId}")
    public ResponseEntity<String> updatePin(@PathVariable Long pinId,
                                            @RequestBody PinRequestDto pinRequestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return pinService.updatePin(pinId, pinRequestDto, userDetails);
    }

    @DeleteMapping("/pin/{pinId}")
    public ResponseEntity<String> deletePin(@PathVariable Long pinId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return pinService.deletePin(pinId, userDetails);
    }

    @PostMapping("/pin")
    public ResponseEntity<String> createPin(@RequestPart String title,
                                            @RequestPart String content,
                                            @RequestPart MultipartFile image,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PinRequestDto pinRequestDto = new PinRequestDto(title, content);
        return pinService.createPin(pinRequestDto ,image, userDetails);
    }
}
