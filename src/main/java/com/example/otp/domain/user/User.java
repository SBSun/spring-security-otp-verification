package com.example.otp.domain.user;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(name = "account_id")
    private String accountId;   // 로그인 아이디

    @NotNull
    @Column(name = "password")
    private String password;    // 비밀번호

    @Column(name = "auth_key")
    private String authKey;     // 인증키

    @Column(name = "roles")
    private String roles;       // 권한

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    @Builder
    public User(String accountId, String password, String authKey) {
        this.accountId = accountId;
        this.password = password;
        this.authKey = authKey;
        this.roles = "ROLE_USER";
    }
}
