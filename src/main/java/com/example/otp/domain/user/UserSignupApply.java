package com.example.otp.domain.user;


import com.example.otp.global.commons.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_signup_apply")
public class UserSignupApply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_signup_apply_id")
    private Long id;

    @NotBlank
    @Column(name = "email")
    private String email;       // 이메일

    @NotBlank
    @Column(name = "password")
    private String password;    // 비밀번호

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "phone")
    private String phone;

    @NotNull
    @Column(name = "user_type")
    private Character userType;       // 권한

    @Builder
    public UserSignupApply(String email, String password, String name, String phone, UserType userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.userType = userType.getType();
    }
}
