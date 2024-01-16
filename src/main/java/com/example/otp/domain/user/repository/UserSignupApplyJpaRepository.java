package com.example.otp.domain.user.repository;

import com.example.otp.domain.user.UserSignupApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSignupApplyJpaRepository extends JpaRepository<UserSignupApply, Long> {

    boolean existsByEmail(String email);
}
