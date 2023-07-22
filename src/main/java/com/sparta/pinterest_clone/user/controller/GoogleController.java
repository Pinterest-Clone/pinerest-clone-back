package com.sparta.pinterest_clone.user.controller;

import com.sparta.pinterest_clone.user.dto.GoogleRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class GoogleController {
    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.pw}")
    private String googleClientPw;

    @GetMapping("/v1/oauth2/google")
    public String getLoginUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth?client_id="
                + googleClientId
                + "&redirect_uri="
                + "http://localhost:8080/login/oauth2/code/google"
                + "&response_type=code" + "&scope=email profile";
    }

    @GetMapping("/login/oauth2/code/google")
    public String getAuthorizationCode(@RequestParam(value = "code") String code) {

        return code;
    }

    @GetMapping("/google/test")
    public String test() {


    }


}
