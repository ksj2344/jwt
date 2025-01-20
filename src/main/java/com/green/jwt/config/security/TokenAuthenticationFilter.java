package com.green.jwt.config.security;

//AuthenticationFilter : accessToken이 있는지 확인하고 있다면 securityContext에 Authentication을 담는다.

import com.green.jwt.config.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=jwtTokenProvider.resolveToken(request); //Bearer 뗀 토큰 가져오기
        if(token!=null){ //null이라면 아무처리 안하고 넘김
            try {
               SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token)); // securityContext에 Authentication을 담기
            } catch (Exception e) {
                throw new RuntimeException("토큰 만료"); //만료시 예외발생
            }
        }
        filterChain.doFilter(request, response); //다음 필터(usernamepassword authentication filter) 호출 //filterChain 참조
    }
}
