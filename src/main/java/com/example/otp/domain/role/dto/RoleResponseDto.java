package com.example.otp.domain.role.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RoleResponseDto {

    @Getter
    @AllArgsConstructor
    public static class RoleInfo {

        private Long roleId;
        private String roleName;
    }
}
