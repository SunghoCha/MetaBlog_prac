package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.domain.Session;
import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.exception.InvalidRequest;
import com.sh.metablog_prac.exception.InvalidSigninInformation;
import com.sh.metablog_prac.exception.Unauthorized;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.LoginRequest;
import com.sh.metablog_prac.response.SessionResponse;
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

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody LoginRequest request) {
        log.info(">>>login={}", request);
        String accessToken = authService.signin(request);
        return new SessionResponse(accessToken);









    }

}
