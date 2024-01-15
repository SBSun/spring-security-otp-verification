package com.example.otp.domain.user.service;

import com.example.otp.domain.user.User;
import com.example.otp.domain.user.UserAdapter;
import com.example.otp.domain.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + ": 존재하지 않는 email입니다."));

        return new UserAdapter(user);
    }
}
