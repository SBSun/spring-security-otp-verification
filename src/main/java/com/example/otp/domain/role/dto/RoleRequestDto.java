package com.example.otp.domain.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RoleRequestDto
{
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddRolesToUser {

        @NotNull
        private Long userId;

        @NotNull
        private List<Long> roleIds;

    }
}
