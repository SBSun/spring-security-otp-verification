package com.example.otp.user;

import com.example.otp.otp.GoogleOTP;
import com.example.otp.user.repository.UserJooqRepository;
import com.example.otp.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final UserJooqRepository userJooqRepository;

    public List<User> getAllUser() {
        return userJooqRepository.getAllUser();
    }

    public String createUser(UserRequestDto.Signup signupDto) {
        if(userJpaRepository.existsByAccountId(signupDto.getAccountId()))
            throw new IllegalArgumentException("이미 존재하는 계정입니다.");

        HashMap<String, String> map = GoogleOTP.generate(signupDto.getAccountId());
        User user = signupDto.toEntity();
        user.setAuthKey(map.get("key"));
        userJpaRepository.save(user);

        return map.get("qrUrl");
    }

    public void login(UserRequestDto.Login loginDto) {
        User getUser = userJpaRepository.findByAccountId(loginDto.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        // 비밀번호 불일치
        if(!getUser.getPassword().equals(loginDto.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        // 인증키가 존재하지 않을 경우
        if(getUser.getAuthKey().isBlank())
            throw new IllegalArgumentException("인증키가 존재하지 않으므로 다시 QR URL 생성");

        // OTP Code가 틀렸을 경우
        if(!GoogleOTP.verifyOTP(getUser.getAuthKey(), loginDto.getOtpCode()))
            throw new IllegalArgumentException("OTP Code가 일치하지 않습니다.");
    }
}