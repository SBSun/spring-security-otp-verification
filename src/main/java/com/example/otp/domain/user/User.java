package com.example.otp.domain.user;

import com.example.otp.global.commons.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "\"user\"")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    @Column(name = "auth_key")
    private String authKey;     // 인증키

    @Column(name = "user_type")
    private Character userType;       // 권한

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    @Builder
    public User(String email, String password, String name, String phone, String authKey, UserType userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.authKey = authKey;
        this.userType = userType.getType();
    }
}
