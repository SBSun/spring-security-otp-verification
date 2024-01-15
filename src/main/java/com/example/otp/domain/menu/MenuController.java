package com.example.otp.domain.menu;

import com.example.otp.domain.menu.dto.MenuResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    @PreAuthorize("isAuthenticated")
    @GetMapping("/hierarchy")
    public ResponseEntity<List<MenuResponseDto.MenuInfo>> getMenuHierarchy(@RequestParam Long menuId) {
        List<MenuResponseDto.MenuInfo> response = menuService.getMenuHierarchy(menuId);

        return ResponseEntity.ok(response);
    }
}
