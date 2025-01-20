package com.green.jwt;

import com.green.jwt.config.constant.JwtConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest //통합테스트. 스프링이 가진 모든 bean 호출
class JwtApplicationTest {
    @Autowired
    private JwtConst jwtConst;

    @Test
    void objJwtConst(){
        System.out.println(jwtConst);
    }
}