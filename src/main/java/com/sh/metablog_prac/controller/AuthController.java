package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.request.Signup;
import com.sh.metablog_prac.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/public/signup")
    public void singup(@RequestBody Signup signup) {
        authService.signup(signup);
    }

}
