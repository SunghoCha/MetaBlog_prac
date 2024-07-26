package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.exception.InvalidRequest;
import com.sh.metablog_prac.exception.InvalidSigninInformation;
import com.sh.metablog_prac.exception.Unauthorized;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.LoginRequest;
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
    public User login(@RequestBody LoginRequest request) {

        //1. json 아이디/비밀번호 넘어옴
        log.info(">>>login={}", request);

        // 2. DB에서 해당 정보 조회
        authService.signin(request);

        //3. 조회성공시 토큰 응답
        return null;






    }

}
