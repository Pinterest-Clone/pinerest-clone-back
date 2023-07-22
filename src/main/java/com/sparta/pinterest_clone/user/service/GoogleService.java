package com.sparta.pinterest_clone.user.service;

import com.sparta.pinterest_clone.user.dto.GoogleInfoDto;
import com.sparta.pinterest_clone.user.dto.GoogleResponseDto;
import com.sparta.pinterest_clone.user.entity.User;
import com.sparta.pinterest_clone.user.repository.UserRepository;
import com.sparta.pinterest_clone.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class GoogleService {
    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.pw}")
    private String googleClientPw;

    @Value("${google.client.redirect}")
    private String redirectUri;

    @Value("${google.client.api}")
    private String apiKey;

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;


    public String getGoogleLoginForm() {
        return "https://accounts.google.com/o/oauth2/v2/auth?client_id="
                + googleClientId
                + "&redirect_uri="
                + redirectUri
                + "&response_type=code" + "&scope=email%20profile%20openid"
                + "&access_type=offline";
    }

    public String googleLogin(String code) {
        String accessToken = getGoogleAccessToken(code);

        String userInfo = getUserInfo(accessToken);

//        User user = signUpWithKakaoEmail(userInfo);
//
//        String jwt = jwtUtil.createJwt(user.getEmail());

        return userInfo;
    }


//    private User signUpWithKakaoEmail(GoogleInfoDto userInfo) {
//        Long googleId = Long.parseLong(userInfo.getSub());
//        User googleUser = userRepository.findByGoogleId(googleId).orElse(null);
//
//        if(googleUser == null) {
//            String googleEmail = userInfo.getEmail();
//
//        }
//    }

    private String getGoogleAccessToken(String code) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://oauth2.googleapis.com")
                .path("/token")
                .build()
                .toUri();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientPw);
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", redirectUri);

        RequestEntity<MultiValueMap<String, String>>
                requestEntity = RequestEntity.post(uri).body(params);

        ResponseEntity<GoogleResponseDto> response = restTemplate.exchange(requestEntity, GoogleResponseDto.class);

        return response.getBody().getAccess_token();
    }

//    private GoogleInfoDto getUserInfo(String accessToken) {
//        URI uri = UriComponentsBuilder
//                .fromUriString("https://oauth2.googleapis.com")
//                .path("/tokeninfo")
//                .build()
//                .toUri();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", JwtUtil.BEARER_PREFIX + accessToken);
//
//        RequestEntity<?> profileRequest = RequestEntity.get(uri).headers(headers).build();
//
//        ResponseEntity<GoogleInfoDto> response = restTemplate.exchange(profileRequest, GoogleInfoDto.class);
//        return response.getBody();
//    }

    private String getUserInfo(String accessToken) {
//        URI uri = UriComponentsBuilder
//                .fromUriString("https://oauth2.googleapis.com")
//                .path("/tokeninfo")
//                .build()
//                .toUri();
//
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", JwtUtil.BEARER_PREFIX + accessToken);
        headers.add("Accept", "application/json");
//
//        RequestEntity<?> profileRequest = RequestEntity.get(uri).headers(headers).build();

//        ResponseEntity<String> response = restTemplate.exchange(profileRequest, String.class);

        URI birthDayUri = UriComponentsBuilder
                .fromUriString("https://people.googleapis.com")
                .path("/v1/people/me")
                .queryParam("personFields", "birthdays")
                .queryParam("key", apiKey)
                .build()
                .toUri();

        RequestEntity<?> birthdayRequest = RequestEntity.get(birthDayUri).headers(headers).build();

        ResponseEntity<String> birthday = restTemplate.exchange(birthdayRequest, String.class);

        return birthday.getBody();
    }


}
