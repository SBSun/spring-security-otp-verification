package com.example.otp.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RoleUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_user_id")
    private Long id;

    @Column(name = "menu_id")
    private Long userId;

    @Column(name = "role_menu_id")
    private Long roleMenuId;
}
