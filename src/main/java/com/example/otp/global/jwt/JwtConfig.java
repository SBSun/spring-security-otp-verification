package com.example.otp.global.jwt;

import com.example.otp.domain.user.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public TokenProvider tokenProvider(CustomUserDetailService userDetailsService){
        return new TokenProvider(userDetailsService);
    }
}
