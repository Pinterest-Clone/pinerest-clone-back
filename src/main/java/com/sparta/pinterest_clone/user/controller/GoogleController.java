package com.sparta.pinterest_clone.user.controller;

import com.sparta.pinterest_clone.user.dto.GoogleInfoDto;
import com.sparta.pinterest_clone.user.dto.GoogleResponseDto;
import com.sparta.pinterest_clone.user.service.GoogleService;
import com.sparta.pinterest_clone.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class GoogleController {
    private final GoogleService googleService;

    @GetMapping("/api/v1/oauth2/google")
    public String getLoginUrl() {
        return googleService.getGoogleLoginForm();
    }

    @GetMapping("/login/oauth2/code/google")
    public void getAuthorizationCode(@RequestParam(value = "code") String code, HttpServletResponse response) {
        String jwt =  googleService.googleLogin(code);

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, jwt.substring(7));
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
