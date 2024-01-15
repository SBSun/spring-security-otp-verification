package com.example.otp.domain.menu.repository;

import com.example.otp.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

    boolean existsById(Long id);
}
