package com.example.otp.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class UserResponseDto {

    @Getter
    @AllArgsConstructor
    public static class Token {

        private String accessToken;
    }

    @Getter
    @AllArgsConstructor
    public static class Login {

        private Boolean isFirstLogin;
        private String qrUrl;
    }

    @Getter
    @AllArgsConstructor
    public static class Info {

        private Long id;
        private String name;
        private String phone;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class SignupApply {

        private Long id;
        private String email;
        private String name;
        private String phone;
        private Character userType;
        private LocalDateTime createdAt;
    }
}
