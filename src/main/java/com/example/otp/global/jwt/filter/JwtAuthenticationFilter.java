package com.example.otp.global.jwt.filter;

import com.example.otp.global.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Request Header에서 토큰 추출
        String accessToken = tokenProvider.resolveToken(request);

        // 2. 토큰 유효성 체크
        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            // 토큰에서 Username, 권한을 추출하여 스프링 시큐리티 유저를 만들어 Authentication 반환
            Authentication authentication = tokenProvider.getAuthentication(accessToken);

            // 해당 스프링 시큐리티 유저를 컨텍스트에 저장, DB를 거치지 않는다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 {} 인증 정보를 저장했습니다. uri: {}", authentication.getName(), request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}
