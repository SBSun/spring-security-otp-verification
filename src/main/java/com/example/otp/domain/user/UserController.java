package com.example.otp.domain.user;

import com.example.otp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<String> signup(@RequestBody UserRequestDto.Signup signupDto) {
        String qrUrl = userService.createUser(signupDto);
        return ResponseEntity.ok(qrUrl);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto.Login loginDto) {
        String token = userService.login(loginDto);
        return ResponseEntity.ok(token);
    }
}
