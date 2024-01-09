package com.example.otp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults())
                .httpBasic(httpBasic -> httpBasic.disable());

        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//        // Spring Security 6.1.0부터는 메서드 체이닝의 사용을 지양하고 람다식을 통해 함수형으로 설정하게 지향
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers(
//                        new AntPathRequestMatcher("/"),
//                        new AntPathRequestMatcher("/h2/**"),
//                        new AntPathRequestMatcher("/h2-console/**"),
//                        new AntPathRequestMatcher("/swagger-ui/**"),
//                        new AntPathRequestMatcher("/error"),
//                        // 일단 모든 API 허용되게 설정
//                        new AntPathRequestMatcher("/**")
//
//                ).permitAll()
//                .anyRequest().authenticated()
//        );

        http.headers((headers -> headers
                .frameOptions(frameOptionsConfig ->
                        frameOptionsConfig.disable()
                )
        ));

        return http.build();
    }
}
