package com.example.otp.domain.menu;

import com.example.otp.domain.menu.dto.MenuResponseDto;
import com.example.otp.domain.menu.repository.MenuJooqRepository;
import com.example.otp.domain.menu.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuJpaRepository menuJpaRepository;
    private final MenuJooqRepository menuJooqRepository;

    public List<MenuResponseDto.MenuInfo> getMenuHierarchy(Long menuId) {
        if (!menuJpaRepository.existsById(menuId)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }

        return menuJooqRepository.getMenuHierarchy(menuId);
    }
}
