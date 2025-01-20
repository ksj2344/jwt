package com.green.jwt.user;

import com.green.jwt.config.CookieUtils;
import com.green.jwt.config.constant.JwtConst;
import com.green.jwt.config.jwt.JwtTokenProvider;
import com.green.jwt.config.jwt.JwtUser;
import com.green.jwt.user.model.UserSelOne;
import com.green.jwt.user.model.UserSignInReq;
import com.green.jwt.user.model.UserSignInRes;
import com.green.jwt.user.model.UserSignUpReq;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;
    private final JwtTokenProvider tokenProvider;
    private final CookieUtils cookieUtils;
    private final JwtConst jwtConst;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(UserSignUpReq req){
        String hashedPw=passwordEncoder.encode(req.getPw());
        req.setPw(hashedPw);

        transactionTemplate.execute(status -> {  //일부만 트랜잭션에 포함되게 함.
            userMapper.insUser(req);
            userMapper.insUserRole(req);

            return null;
        });
    }

    public UserSignInRes signIn(UserSignInReq req, HttpServletResponse response){
        UserSelOne userSelOne=userMapper.selUserWithRoles(req).orElseThrow(()->{
            throw new RuntimeException("아이디를 확인해주세요.");
        });

        if(!passwordEncoder.matches(req.getPw(),userSelOne.getPw())){
            throw new RuntimeException("비밀번호를 확인해주세요.");
        }

        JwtUser jwtUser = new JwtUser(userSelOne.getId(), userSelOne.getRoles());
        String accessToken=tokenProvider.generateAccessToken(jwtUser);
        String refreshToken=tokenProvider.generateRefreshToken(jwtUser);

        //refreshToken을 cookie에 담기
        cookieUtils.setCookie(response, jwtConst.getRefreshTokenCookieName(),refreshToken,jwtConst.getRefreshTokenCookieExpiry());

        return UserSignInRes.builder()
                .id(userSelOne.getId())
                .name(userSelOne.getName())
                .accessToken(accessToken)
                .build();
    }

    public String getAccessToken(HttpServletRequest req){ //쿠키에 담긴 refreshtoken으로 accesstoken 발행하기
        Cookie cookie = Optional.ofNullable(cookieUtils.getCookie(req, jwtConst.getRefreshTokenCookieName()))
                .orElseThrow(()-> {
                    throw new RuntimeException("AccessToken을 재발행 할 수 없습니다.");
                });
        String refreshToken=cookie.getValue();
        JwtUser jwtUser=jwtTokenProvider.getJwtUserFromToken(refreshToken);

        return jwtTokenProvider.generateAccessToken(jwtUser);
    }
}
