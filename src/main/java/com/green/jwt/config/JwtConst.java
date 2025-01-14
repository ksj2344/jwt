package com.green.jwt.config;

//yml의 const 객체 가져오는 작업

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt-const") //application에 @ConfigurationPropertiesScan 붙일것
@RequiredArgsConstructor
@ToString //Test에서 잘 넘어오나 확인하려고 붙임
public class JwtConst {
    private final String issuer;
    private final String secret;
    private final String headerSchemaName;
    private final String claimKey;
    private final String tokenName;
    private final String tokenType;
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;
    private final String refreshTokenCookieName;
    private final int refreshTokenCookieExpiry;
}
