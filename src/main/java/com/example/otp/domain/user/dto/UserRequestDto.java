package com.example.otp.domain.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Signup{

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
        private String password;

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        private String name;

        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        private String phone;

        @NotNull(message = "사용자 타입은 필수 입력 값입니다.")
        private Character userType;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login{

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Otp{

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
        private String password;

        @NotBlank(message = "OTP Code는 필수 입력 값입니다.")
        private String otpCode;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueQRUrl{

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
        private String password;
    }
}
