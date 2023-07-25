package com.sparta.pinterest_clone.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.pinterest_clone.user.dto.GoogleInfoDto;
import com.sparta.pinterest_clone.user.dto.GoogleResponseDto;
import com.sparta.pinterest_clone.user.entity.GoogleUserInfo;
import com.sparta.pinterest_clone.user.entity.User;
import com.sparta.pinterest_clone.user.repository.UserRepository;
import com.sparta.pinterest_clone.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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
    private final PasswordEncoder passwordEncoder;


    public String getGoogleLoginForm() {
        return "https://accounts.google.com/o/oauth2/v2/auth?client_id="
                + googleClientId
                + "&redirect_uri="
                + redirectUri
                + "&response_type=code" + "&scope=email%20profile%20openid%20https://www.googleapis.com/auth/user.birthday.read"
                + "&access_type=offline";
    }

    public String googleLogin(String code) {
        String accessToken = getGoogleAccessToken(code);

        GoogleUserInfo userInfo = getUserInfo(accessToken);

        User user = signUpWithGoogleEmail(userInfo);
//
        String jwt = jwtUtil.createJwt(user.getEmail());

        return jwt;
    }


    private User signUpWithGoogleEmail(GoogleUserInfo userInfo) {
        // 한 번이라도 google로그인 했는지 체크
        String googleId = userInfo.getGoogleId();
        User googleUser = userRepository.findByGoogleId(googleId).orElse(null);

        if(googleUser == null) { // 회원가입 해야함
            String googleEmail = userInfo.getEmail();
            User existedUserWithSameEmail = userRepository.findByEmail(googleEmail).orElse(null);
            // 구글 계정과 똑같은 계정이 db에 존재할 경우
            if(existedUserWithSameEmail != null) {
                googleUser = existedUserWithSameEmail;
                googleUser = googleUser.updateGoogleId(googleId);
            }
            // 신규회원일 경우
            else {
                String email = userInfo.getEmail();
                String password = passwordEncoder.encode(UUID.randomUUID().toString());

                googleUser = new User(email, password, userInfo.getBirthday(), googleId);
            }
            userRepository.save(googleUser);
        }

        return googleUser;
    }

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


    private GoogleUserInfo getUserInfo(String accessToken) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://oauth2.googleapis.com")
                .path("/tokeninfo")
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", JwtUtil.BEARER_PREFIX + accessToken);
        headers.add("Accept", "application/json");

        RequestEntity<?> profileRequest = RequestEntity.get(uri).headers(headers).build();

        ResponseEntity<GoogleInfoDto> profileResponse = restTemplate.exchange(profileRequest, GoogleInfoDto.class);
        GoogleInfoDto googleInfoDto = profileResponse.getBody();

        String birthday = getBirthday(accessToken);

        GoogleUserInfo googleUserInfo = new GoogleUserInfo(googleInfoDto.getSub(), googleInfoDto.getEmail(), birthday);
        return googleUserInfo;
    }

    private String getBirthday(String accessToken) {
        URI birthDayUri = UriComponentsBuilder
                .fromUriString("https://people.googleapis.com")
                .path("/v1/people/me")
                .queryParam("personFields", "birthdays")
                .queryParam("key", apiKey)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", JwtUtil.BEARER_PREFIX + accessToken);
        headers.add("Accept", "application/json");

        RequestEntity<?> birthdayRequest = RequestEntity.get(birthDayUri).headers(headers).build();

        ResponseEntity<String> birthdayResponse = restTemplate.exchange(birthdayRequest, String.class);

        JsonNode jsonNode;
        try {
            jsonNode = new ObjectMapper().readTree(birthdayResponse.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        jsonNode = jsonNode.get("birthdays").get(0).get("date");
        String year = jsonNode.get("year").asText();
        String month = jsonNode.get("month").asText();
        String day = jsonNode.get("day").asText();

        return year + "-" + month + "-" + day;
    }


}
