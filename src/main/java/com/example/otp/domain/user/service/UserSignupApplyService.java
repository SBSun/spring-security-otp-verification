package com.example.otp.domain.user.service;

import com.example.otp.domain.user.UserSignupApply;
import com.example.otp.domain.user.UserType;
import com.example.otp.domain.user.dto.UserRequestDto;
import com.example.otp.domain.user.dto.UserResponseDto;
import com.example.otp.domain.user.repository.UserJpaRepository;
import com.example.otp.domain.user.repository.UserSignupApplyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserSignupApplyService {

    private final UserSignupApplyJpaRepository userSignupApplyJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 요청 저장
     * @param signupApplyDto
     */
    @Transactional
    public void signupApply(UserRequestDto.SignupApply signupApplyDto) {
        if (userJpaRepository.existsByEmail(signupApplyDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 계정입니다.");
        }

        if (userSignupApplyJpaRepository.existsByEmail(signupApplyDto.getEmail())) {
            throw new IllegalArgumentException("이미 회원가입 요청한 계정입니다.");
        }

        UserSignupApply userSignupApply = UserSignupApply.builder()
                .email(signupApplyDto.getEmail())
                .password(passwordEncoder.encode(signupApplyDto.getPassword()))
                .name(signupApplyDto.getName())
                .phone(signupApplyDto.getPhone())
                .userType(UserType.of(signupApplyDto.getUserType()))
                .build();

        userSignupApplyJpaRepository.save(userSignupApply);
    }

    public List<UserResponseDto.SignupApply> findAll() {
        return userSignupApplyJpaRepository.findAll()
                .stream()
                .map(apply -> new UserResponseDto.SignupApply(
                        apply.getId(),
                        apply.getEmail(),
                        apply.getName(),
                        apply.getPhone(),
                        apply.getUserType(),
                        apply.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
