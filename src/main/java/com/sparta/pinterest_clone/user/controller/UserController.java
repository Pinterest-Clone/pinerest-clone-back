package com.sparta.pinterest_clone.user.controller;

import com.sparta.pinterest_clone.pin.dto.PinResponseDto;
import com.sparta.pinterest_clone.security.UserDetailsImpl;
import com.sparta.pinterest_clone.user.dto.LoginRequestDto;
import com.sparta.pinterest_clone.user.dto.UpdateProfileRequestDto;
import com.sparta.pinterest_clone.user.dto.UpdateProfileResponseDto;
import com.sparta.pinterest_clone.user.dto.UserPageResponseDto;
import com.sparta.pinterest_clone.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final Boolean WANT_SAVED_PIN = Boolean.TRUE;
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody LoginRequestDto loginRequestDto){
        userService.signup(loginRequestDto);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/settings/profile")
    public UpdateProfileResponseDto updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @RequestPart(required = false) MultipartFile userImage,
                                                  @ModelAttribute UpdateProfileRequestDto requestDto) {

        return userService.updateProfile(userDetails, requestDto, userImage);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<UserPageResponseDto> getUserPage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable String nickname) {

        return ResponseEntity.ok(userService.getUserPage(userDetails, nickname));
    }

    @GetMapping("/{nickname}/saved")
    public ResponseEntity<List<PinResponseDto>> getSavedPins(@PathVariable String nickname) {

        return ResponseEntity.ok(userService.getPinsByUser(nickname, WANT_SAVED_PIN));
    }

    @GetMapping("/{nickname}/created")
    public ResponseEntity<List<PinResponseDto>> getCreatedPins(@PathVariable String nickname) {

        return ResponseEntity.ok(userService.getPinsByUser(nickname, !WANT_SAVED_PIN));
    }
}
