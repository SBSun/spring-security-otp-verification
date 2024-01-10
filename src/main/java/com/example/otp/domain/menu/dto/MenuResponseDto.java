package com.example.otp.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MenuResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MenuInfo {
        private Long id;
        private String name;
        private Long ancestorId;
    }
}
