package com.example.otp.domain.user.service;

import com.example.otp.domain.user.User;
import com.example.otp.domain.user.UserAdapter;
import com.example.otp.domain.user.UserType;
import com.example.otp.domain.user.dto.UserResponseDto;
import com.example.otp.domain.user.repository.UserJooqRepository;
import com.example.otp.domain.user.repository.UserJpaRepository;
import com.example.otp.global.otp.GoogleOTP;
import com.example.otp.domain.user.dto.UserRequestDto;
import com.example.otp.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
    private final UserJooqRepository userJooqRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser() {
        return userJpaRepository.findAll();
    }

    public List<UserResponseDto.Info> search(String name, String phone) {
        return userJooqRepository.search(name, phone);
    }

    /**
     * 회원가입
     * @param signupDto
     */
    @Transactional
    public void createUser(UserRequestDto.Signup signupDto) {
        if (userJpaRepository.existsByEmail(signupDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 계정입니다.");
        }

        User user = User.builder()
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .name(signupDto.getName())
                .phone(signupDto.getPhone())
                .userType(UserType.of(signupDto.getUserType()))
                .authKey(null)
                .build();

        userJpaRepository.save(user);
    }

    /**
     * 로그인(아이디 비밀번호)
     * 첫 로그인(O): 첫 로그인이라는 true 값과 QR Url 반환
     * 첫 로그인(X): 첫 로그인이 아니라는 false 값과 null 반환
     */
    @Transactional
    public UserResponseDto.Login login(UserRequestDto.Login loginDto) {
        User user = userJpaRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        UserResponseDto.Login responseDto;

        // Auth Key가 존재하지 않으면 첫 로그인 시도
        if (!StringUtils.hasText(user.getAuthKey())) {
            // 첫 로그인이라는 boolean 값과 QRUrl 반환
            HashMap<String, String> map = GoogleOTP.generate(loginDto.getEmail());
            user.setAuthKey(map.get(AUTH_KEY));
            responseDto = new UserResponseDto.Login(true, map.get(QR_URL));
        } else {
            responseDto = new UserResponseDto.Login(false, null);
        }

        return responseDto;
    }

    /**
     * OTP 인증
     * 아이디, 비밀번호, OTP Code 검증
     */
    public UserResponseDto.Token authenticateOtp(UserRequestDto.Otp otpDto) {
        // 입력한 accountId와 password로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(otpDto.getEmail(), otpDto.getPassword());

        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService의 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        UserAdapter userAdapter = (UserAdapter)authentication.getPrincipal();
        User user = userAdapter.getUser();

        if (!GoogleOTP.verifyOTP(user.getAuthKey(), otpDto.getOtpCode())) {
            throw new IllegalArgumentException("OTP Code가 일치하지 않습니다.");
        }

        String accessToken = tokenProvider.createAccessToken(authentication);
        return new UserResponseDto.Token(accessToken);
    }

    @Transactional
    public String reissueQRUrl(UserRequestDto.ReissueQRUrl reissueQRUrlDto) {
        User user = userJpaRepository.findByEmail(reissueQRUrlDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(reissueQRUrlDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        HashMap<String, String> map = GoogleOTP.generate(reissueQRUrlDto.getEmail());
        user.setAuthKey(map.get(AUTH_KEY));

        return map.get(QR_URL);
    }
}