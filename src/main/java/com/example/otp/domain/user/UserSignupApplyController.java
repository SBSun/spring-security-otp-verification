package com.example.otp.domain.user;

import com.example.otp.domain.user.dto.UserRequestDto;
import com.example.otp.domain.user.dto.UserResponseDto;
import com.example.otp.domain.user.service.UserSignupApplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users/signup/apply")
public class UserSignupApplyController {

    private final UserSignupApplyService userSignupApplyService;

    @PreAuthorize("isAnonymous()")
    @PostMapping
    public ResponseEntity<Void> signupApply(@RequestBody @Valid UserRequestDto.SignupApply signupApplyDto) {
        userSignupApplyService.signupApply(signupApplyDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<UserResponseDto.SignupApply>> findAll() {
        List<UserResponseDto.SignupApply> response = userSignupApplyService.findAll();
        return ResponseEntity.ok(response);
    }
}
