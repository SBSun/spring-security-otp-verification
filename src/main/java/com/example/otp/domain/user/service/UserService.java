package com.example.otp.domain.user.service;

import com.example.otp.domain.user.User;
import com.example.otp.domain.user.UserAdapter;
import com.example.otp.domain.user.dto.UserResponseDto;
import com.example.otp.domain.user.repository.UserJpaRepository;
import com.example.otp.global.otp.GoogleOTP;
import com.example.otp.domain.user.dto.UserRequestDto;
import com.example.otp.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

import static com.example.otp.global.otp.GoogleOTP.AUTH_KEY;
import static com.example.otp.global.otp.GoogleOTP.QR_URL;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser() {
        return userJpaRepository.findAll();
    }

    @Transactional
    public String createUser(UserRequestDto.Signup signupDto) {
        if (userJpaRepository.existsByAccountId(signupDto.getAccountId())) {
            throw new IllegalArgumentException("이미 존재하는 계정입니다.");
        }

        HashMap<String, String> map = GoogleOTP.generate(signupDto.getAccountId());
        User user = User.builder()
                .accountId(signupDto.getAccountId())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .authKey(map.get(AUTH_KEY))
                .build();

        userJpaRepository.save(user);

        return map.get(QR_URL);
    }

    /**
     * 로그인
     */
    public UserResponseDto.Token login(UserRequestDto.Login loginDto) {
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

        return new UserResponseDto.Token(tokenProvider.createAccessToken(authentication));
    }

    @Transactional
    public String reissueQRUrl(UserRequestDto.ReissueQRUrl reissueQRUrlDto) {
        User user = userJpaRepository.findByAccountId(reissueQRUrlDto.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(reissueQRUrlDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        HashMap<String, String> map = GoogleOTP.generate(reissueQRUrlDto.getAccountId());
        user.setAuthKey(map.get(AUTH_KEY));

        return map.get(QR_URL);
    }
}