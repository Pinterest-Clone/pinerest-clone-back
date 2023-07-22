package com.sparta.pinterest_clone.pin.service;

import com.sparta.pinterest_clone.pin.PinRepository.PinRepository;
import com.sparta.pinterest_clone.pin.dto.PinRequestDto;
import com.sparta.pinterest_clone.pin.entity.Pin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;

    public ResponseEntity<String> createPin(PinRequestDto pinRequestDto) {

//        User
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));

        MultipartFile files = pinRequestDto.getImageFile();

//        Pin pin = new pin(pinRequestDto)
        return ResponseEntity.ok("핀 등록 완료.");


    }
}
