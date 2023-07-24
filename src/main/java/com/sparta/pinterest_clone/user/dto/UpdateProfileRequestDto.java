package com.sparta.pinterest_clone.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequestDto {
    // private String 프로필 사진

    @NotBlank
    @Size(min=1, max=30, message = "30자 이하로 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "이름은 영어, 한글만 입력할 수 있습니다.")
    private String firstname;

    @Size(max=30, message = "30자 이하로 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "성은 영어, 한글만 입력할 수 있습니다.")
    private String lastname;

    @Size(min=1, max=500, message = "500자 이하로 입력하세요.")
    private String introduction;

//    @Pattern(regexp = "^https?://.+\\..+$", message = "올바른 URL 형식이 아닙니다.")
    @Size(max = 200, message = "URL은 200자 이하로 입력하세요.")
    private String myUrl;

    @NotBlank
    @Size(min=3, max=30, message = "사용자 이름은 3~30자여야 합니다.")
    private String username;

}
