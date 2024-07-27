package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.config.SecurityConfig;
import com.sh.metablog_prac.request.LoginRequest;
import com.sh.metablog_prac.response.SessionResponse;
import com.sh.metablog_prac.service.AuthService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseCookie> login(@RequestBody LoginRequest request) {
        log.info(">>>login={}", request);
        String accessToken = authService.signin(request);
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost") // todo domain은 개발환경에 따른 변수값을 yml에 지정하고 불러오는 방식으로 하면 좋음 로컬,배포 등..
                .path("/") // 쿠키의 유효 경로
                .httpOnly(true) // : JavaScript에서 접근할 수 없도록 쿠키의 HttpOnly 속성을 설정
                .secure(false) //  HTTPS 연결을 사용하는 경우에만 쿠키를 전송하도록 설정
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict") // 크로스 사이트 요청에서 쿠키를 전송하는 방식을 제어 (동일 출처 요청에서만 쿠키 전송)
                .build();
        log.info(">>>>> cookie = {}", cookie.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/auth/v2/login")
    public SessionResponse loginV2(@RequestBody LoginRequest request) {
        log.info(">>>loginV2={}", request);
        Long userId = authService.signinV2(request);
        String jws = Jwts.builder().subject(String.valueOf(userId)).signWith(SecurityConfig.KEY).compact();

        return new SessionResponse(jws);


    }

}
