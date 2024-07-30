package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.config.AppConfig;
import com.sh.metablog_prac.request.Signup;
import com.sh.metablog_prac.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @GetMapping("/auth/login")
    public String login() {
        String test = "1234";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>> Authenticated: {}", authentication);
            test = "00000000";
        } else {
            log.info(">>>>>>>>>>>>>>>>>>>>>>> Authentication failed or user not authenticated.");
            test = "11111111";
        }

        return "로그인 페이지입니다." + test;
    }


    @PostMapping("/public/signup")
    public void singup(@RequestBody Signup signup) {
        authService.signup(signup);
    }

}
