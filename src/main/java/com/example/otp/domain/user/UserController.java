package com.example.otp.domain.user;

import com.example.otp.domain.user.dto.UserRequestDto;
import com.example.otp.domain.user.dto.UserResponseDto;
import com.example.otp.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PreAuthorize("isAnonymous")
    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody @Validated UserRequestDto.Signup signupDto) {
        userService.createUser(signupDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAnonymous")
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto.Login> login(@RequestBody @Valid UserRequestDto.Login loginDto) {
        UserResponseDto.Login responseDto = userService.login(loginDto);

        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("isAnonymous")
    @PostMapping("/authenticate/otp")
    public ResponseEntity<UserResponseDto.Token> authenticateOtp(@RequestBody @Valid UserRequestDto.Otp otpDto) {
        UserResponseDto.Token responseDto = userService.authenticateOtp(otpDto);

        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("isAnonymous")
    @PutMapping("/reissue/qr-url")
    public ResponseEntity<String> reissueQRUrl(@RequestBody @Valid UserRequestDto.ReissueQRUrl reissueQRUrlDto) {
        String qrUrl = userService.reissueQRUrl(reissueQRUrlDto );

        return ResponseEntity.ok(qrUrl);
    }
}
