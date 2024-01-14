package com.example.otp.domain.user.service;

import com.example.otp.domain.user.User;
import com.example.otp.domain.user.UserAdapter;
import com.example.otp.domain.user.repository.UserJooqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserJooqRepository userJooqRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {

        User user = userJooqRepository.findByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException(accountId + ": 존재하지 않는 accountId입니다."));

        return new UserAdapter(user);
    }
}
