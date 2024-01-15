package com.example.otp.domain.user;

import com.example.otp.domain.user.dto.UserRequestDto;
import com.example.otp.domain.user.dto.UserResponseDto;
import com.example.otp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<String> signup(@RequestBody UserRequestDto.Signup signupDto) {
        String qrUrl = userService.createUser(signupDto);

        return ResponseEntity.ok(qrUrl);
    }

    @PreAuthorize("isAnonymous")
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto.Token> login(@RequestBody UserRequestDto.Login loginDto) {
        UserResponseDto.Token token = userService.login(loginDto);

        return ResponseEntity.ok(token);
    }

    @PreAuthorize("isAnonymous")
    @PutMapping("/reissue/qr-url")
    public ResponseEntity<String> reissueQRUrl(@RequestBody UserRequestDto.ReissueQRUrl reissueQRUrlDto) {
        String qrUrl = userService.reissueQRUrl(reissueQRUrlDto );

        return ResponseEntity.ok(qrUrl);
    }
}
