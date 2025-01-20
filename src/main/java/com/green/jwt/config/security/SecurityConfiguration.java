package com.green.jwt.config.security;

import com.green.jwt.config.jwt.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration//빈등록, 보통 빈등록 메소드
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    @Bean //스프링이 메소드 호출을하고 리턴한 객체 주소값을 관리한다. (빈등록)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //시큐리티가 세션을 사용하지 않는다.
                .httpBasic(h -> h.disable()) //SSR(sever side rendering)이 아니다. 화면을 만들지 않을 것이므로 비활성화 한다. 시큐리티 로그인창은 나타나지 않을것이다.
                .formLogin(form -> form.disable()) //SSR(sever side rendering)이 아니다. 폼로그인 기능 자체를 비활성화
                .csrf(csrf -> csrf.disable()) //보안관련 SSR이 아니면 보안이슈가 없기 때문에 기능을 끈다.
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req->
                        req.requestMatchers("/api/admin").hasRole(UserRole.ADMIN.name()) //인가 하나처리
                                .requestMatchers("/api/mentor").hasRole(UserRole.MENTOR.name())
                                .requestMatchers(HttpMethod.GET, "/api/user").hasRole(UserRole.USER.name())//sign-up과 sign-in은 userid없어도 되나 get할땐 필요하다
                                .requestMatchers("/api/admin-mentor").hasAnyRole(UserRole.ADMIN.name(), UserRole.MENTOR.name())
                                .requestMatchers("/api/admin", "/api/mentor", "/api/admin-mentor", "/api/user").authenticated()
                                .anyRequest().permitAll() //나머지 허가
                ) //해당 주소로 들어오는 요청이 인증처리(로그인)이 되어있는지 filter를 거치겠다.
                //만약 /api/user/ 로 들어오는거 다 막고 싶다면 ("/api/user/**")
                .build();
    }

}
