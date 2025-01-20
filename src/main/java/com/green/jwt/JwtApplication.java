package com.green.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }

}

/*
    Security는 필터에서 작동한다.
    필터는 (1) 아무런 작업을 하지 않고 다음 필터에게 넘긴다.
          (2) 특정 작업 수행 후 다음 필터에게 넘긴다. *우리로 따지면 Authentication 객체 담기 작업.
          (3) 문제가 있으면 다음 필터에게 넘기지 않고 바로 예외처리 응답을 한다.
    셋 중 하나 동작을 함.

    - 로그인할 때 AT, RT 생성. AT는 Body, RT는 Cookie에 담아서 응답
    - FE에서 AT를 받은 순간부터 모든 요청의 HEADER에 Authorization 키값(const.header-key)으로 "Bearer ${AT}"를 보낸다.
    - 요청이 들어올 때마다 AT 체크를 한다. 이 체크는 Filter에서 함. 여기로 따지면 TokenAuthenticationFilter.
      : HEADER에 Authorization 키가 있는지 확인, 있으면 Bearer를 뺀 AT를 뽑아낸다.
        있다면? >> Token 유효하다면? >>Authentication 객체(principal(정보, 보통 pk)+authorities)를 생성하고 SecurityContextHolder에 담는다.
                       (Spring Framework Security(SFS) 미들웨어가 인증 처리하는 방법.) <-securityFilterChain 클래스에서 인증체크함
                       즉, 모든 요청마다 Authentication 객체가 SecurityContextHolder에 담겨있어야 인증이 되었다고 처리한다.)
               >> Token 만료되었다면? 예외를 발생한다. 403을 응답함.
        없다면? >> 아무런 작업을 하지않고 SFS가 인증/인가 처리되는 URL은 사용할 수 없게 된다.
                  인가와 인증이 필요없는 URL은 사용할 수 있음.


*/