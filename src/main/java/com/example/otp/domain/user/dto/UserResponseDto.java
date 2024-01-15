package com.example.otp.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserResponseDto {

    @Getter
    @AllArgsConstructor
    public static class Token {

        private String accessToken;
    }
}
