package com.sh.metablog_prac.service;

import com.sh.metablog_prac.domain.Session;
import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.exception.InvalidSigninInformation;
import com.sh.metablog_prac.repository.SessionRepository;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public String signin(LoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();// 명령과 조회가 같이 있는 느낌.. 값을 저장하면서 동시에 반환?
        return session.getAccessToken();
    }
}
