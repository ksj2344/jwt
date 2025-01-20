package com.green.jwt.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class CookieUtils {
    //Req header에서 내가 원하는 쿠키를 찾는 메소드
    public Cookie getCookie(HttpServletRequest req, String name) {
        Optional<Cookie[]> optCookie = Optional.ofNullable(req.getCookies()); //Optional.ofNullable: null을 가질 수 있는 옵셔널
        return Arrays.stream(optCookie.orElseThrow(()->new RuntimeException("Cookie not found")))//Stream<Cookie[]>
                .filter(item->item.getName().equals(name)) //Predicate 인터페이스 구현화 //필터 조건에 맞는 stream 리턴(중간연산)
                .findFirst() //첫번째 item 선택(최종연산)
                .orElseThrow(()->new RuntimeException("Cookie not found"));

        //마지막 결과물을 반환하는 것이 최종연산

    }

    //Res header에 내가 원하는 쿠키를 넣는 메소드
    public void setCookie(HttpServletResponse res, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/api/user/access-token"); //이 요청으로 들어올 때만 쿠키값이 넘어올 수 있도록.
        cookie.setHttpOnly(true); //보안 쿠키 설정, 프론트에서 JS로 쿠키값을 얻을 수 없다.
        cookie.setMaxAge(maxAge);
        res.addCookie(cookie);
    }
}
