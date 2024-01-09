package com.example.otp.menu;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(name = "upper_menu_id")
    private Long upperMenuId;

    @Column(name = "menu_name")
    private String name;

    @Column(name = "menu_url")
    private String menuUrl;

    @Column(name = "menu_order")
    private Integer order;

    @Column(name = "menu_desc")
    private String description;
}