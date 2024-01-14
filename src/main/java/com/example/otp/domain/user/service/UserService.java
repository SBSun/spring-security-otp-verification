package com.example.otp.domain.user.service;

import com.example.otp.domain.user.User;
import com.example.otp.domain.user.UserAdapter;
import com.example.otp.domain.user.repository.UserJpaRepository;
import com.example.otp.global.otp.GoogleOTP;
import com.example.otp.domain.user.UserRequestDto;
import com.example.otp.domain.user.repository.UserJooqRepository;
import com.example.otp.global.jwt.TokenProvider;
import com.example.otp.global.security.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final UserJooqRepository userJooqRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser() {
        return userJooqRepository.getAllUser();
    }

    public String createUser(UserRequestDto.Signup signupDto) {
        if (userJpaRepository.existsByAccountId(signupDto.getAccountId())) {
            throw new IllegalArgumentException("이미 존재하는 계정입니다.");
        }

        HashMap<String, String> map = GoogleOTP.generate(signupDto.getAccountId());
        User user = User.builder()
                .accountId(signupDto.getAccountId())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .authKey(map.get("key"))
                .build();

        userJpaRepository.save(user);

        return map.get("qrUrl");
    }

    /**
     * 로그인
     */
    public String login(UserRequestDto.Login loginDto) {
        // 입력한 accountId와 password로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getAccountId(), loginDto.getPassword());

        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService의 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        UserAdapter userAdapter = (UserAdapter)authentication.getPrincipal();
        User user = userAdapter.getUser();

        // Auth Key가 존재하지 않거나 OTP Code가 일치하지 않을 경우
        if (!StringUtils.hasText(user.getAuthKey()) ||
                !GoogleOTP.verifyOTP(user.getAuthKey(), loginDto.getOtpCode())) {
            throw new IllegalArgumentException("인증키가 존재하지 않거나 OTP Code가 일치하지 않으므로 다시 QR URL 생성");
        }

        return tokenProvider.createAccessToken(authentication);
    }
}