package com.green.jwt.user;

import com.green.jwt.user.model.UserSignInReq;
import com.green.jwt.user.model.UserSignInRes;
import com.green.jwt.user.model.UserSignUpReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @PostMapping("sign-up")
    public long signUp(@RequestBody UserSignUpReq req){
        log.info("sign-up req: {}", req);
        userService.signUp(req);
        return req.getId();
    }


    @PostMapping("sign-in")
    public UserSignInRes signIn(@RequestBody UserSignInReq req, HttpServletResponse response){
        log.info("sign-in req: {}", req);
        return userService.signIn(req, response);
    }

    /*
        /api/user/access-token인 이유. cookieUtils에서 요청받을 경로 지정해뒀으니 맞춰야함
    */
    @GetMapping("access-token")
    public String getAccessToken(HttpServletRequest req){ //access토큰이 만료되고 refresh토큰이 있다면 호출됨.
        return userService.getAccessToken(req);
    }

    @GetMapping
    public String get(){
        return "user";
    }
}
