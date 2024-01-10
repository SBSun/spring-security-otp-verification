package com.example.otp.domain.role.service;

import com.example.otp.domain.role.dto.RoleResponseDto;
import com.example.otp.domain.user.repository.UserJpaRepository;
import com.example.otp.domain.role.dto.RoleRequestDto;
import com.example.otp.domain.role.repository.RoleUserJooqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleUserService {

    private final RoleUserJooqRepository roleUserJooqRepository;
    private final UserJpaRepository userJpaRepository;

    public void addRolesToUser(RoleRequestDto.AddRolesToUser requestDto) {
        if (!userJpaRepository.existsById(requestDto.getUserId())) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        roleUserJooqRepository.addRolesToUser(requestDto.getUserId(), requestDto.getRoleIds());
    }

    public List<RoleResponseDto.RoleInfo> getRolesByUser(Long userId) {
        if (!userJpaRepository.existsById(userId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        return roleUserJooqRepository.getRolesByUser(userId);
    }

    public List<RoleResponseDto.RoleInfo> getRolesByUserAndMenu(Long userId, Long menuId) {
        if (!userJpaRepository.existsById(userId)){
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        if (!roleUserJooqRepository.is3DepthMenu(menuId)) {
            throw new IllegalArgumentException("3Depth에 해당하는 메뉴가 아닙니다.");
        }

        return roleUserJooqRepository.getRolesByUserAndMenu(userId, menuId);
    }
}
