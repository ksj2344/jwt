package com.green.jwt.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jwt.config.JwtConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final ObjectMapper objectMapper;
    private final SecretKey secretKey;
    private final JwtConst jwtConst;

    public JwtTokenProvider(ObjectMapper objectMapper, JwtConst jwtConst) {
        this.objectMapper = objectMapper;
        this.jwtConst = jwtConst;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConst.getSecret()));
        //hmac: 대칭키, sha: 암호화 방법  //encode: 암호화, decode: 복호화
    }

    public String generateAccessToken(JwtUser jwtUser) {
        return generateToken(jwtUser,jwtConst.getAccessTokenExpiry());
    }

    public String generateRefreshToken(JwtUser jwtUser) {
        return generateToken(jwtUser,jwtConst.getRefreshTokenExpiry());
    }

    public String generateToken(JwtUser jwtUser, long tokenValidMilliSecond) {
        Date now = new Date();
        return Jwts.builder()
                    //header
                .header().type(jwtConst.getTokenName())
                .and()
                    //paload
                .issuer(jwtConst.getIssuer()) //토큰 발행자
                .issuedAt(now)
                .expiration(new Date(now.getTime()+tokenValidMilliSecond))  //만료시간
                .claim(jwtConst.getClaimKey(), makeClaimByUserToString(jwtUser))  //로그인한 유저 id와 권한 정보
                    //signature
                .signWith(secretKey)
                .compact();
    }

    //객체를 String으로 바꾸는 작업: 직렬화(JSON)
    private String makeClaimByUserToString(JwtUser jwtUser) {
        //객체 자체를 JWT에 담고 싶어서 객체를 직렬화
        //jwtUser에 담고있는 데이터를 JSON 형태의 문자열로 변환
        try {
            return objectMapper.writeValueAsString(jwtUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    //---- 만들어진 토큰(AT,RT)에서 정보 뽑기

    private Claims getClaims(String token) { //payload부분 뽑기
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public JwtUser getJwtUserFromToken(String token) {
        Claims claims = getClaims(token);
        String json=claims.get(jwtConst.getClaimKey(),String.class);  //makeClaimByUserToString(jwtUser)의 정보가 넘어옴
        try {
            return objectMapper.readValue(json, JwtUser.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //Spring Security(securityContext)에서 autentication을 담음. autentication은 principal을 담고 principal은 userDetails와 같음
    //autentication: principal+Authorities
    //이걸 통해 인증함
    public Authentication getAuthentication(String token) {
        JwtUser jwtUser = getJwtUserFromToken(token);
        return new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
    }

}
