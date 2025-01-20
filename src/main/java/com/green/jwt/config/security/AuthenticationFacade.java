package com.green.jwt.config.security;

import com.green.jwt.config.jwt.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationFacade { //Authentication에 있는 signedUserId 가져오기
    public static long getSignedUserId() {
        return ((JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSignedUserId();
    }
}
