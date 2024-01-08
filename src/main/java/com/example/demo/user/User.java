package com.example.demo.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String accountId;   // 로그인 아이디

    @NotNull
    private String password;    // 비밀번호

    private String authKey;     // 인증키

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public User(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }
}
