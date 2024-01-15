package com.example.otp.domain.role.controller;

import com.example.otp.domain.role.dto.RoleRequestDto;
import com.example.otp.domain.role.dto.RoleResponseDto;
import com.example.otp.domain.role.service.RoleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role-user")
public class RoleUserController {

    private final RoleUserService roleUserService;

    @PreAuthorize("isAuthenticated")
    @PostMapping
    public ResponseEntity<Void> addRolesToUser(@RequestBody RoleRequestDto.AddRolesToUser requestDto) {
        roleUserService.addRolesToUser(requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping
    public ResponseEntity<List<RoleResponseDto.RoleInfo>> getRolesByUser(@RequestParam Long userId) {
        List<RoleResponseDto.RoleInfo> response = roleUserService.getRolesByUser(userId);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/menus")
    public ResponseEntity<List<RoleResponseDto.RoleInfo>> getRolesByUserAndMenu(@RequestParam Long userId,
                                                                                @RequestParam Long menuId) {
        List<RoleResponseDto.RoleInfo> response = roleUserService.getRolesByUserAndMenu(userId, menuId);
        return ResponseEntity.ok(response);
    }
}