package com.sh.metablog_prac.service;

import com.sh.metablog_prac.domain.Session;
import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.exception.AlreadyExistsEmailException;
import com.sh.metablog_prac.exception.InvalidRequest;
import com.sh.metablog_prac.exception.InvalidSigninInformation;
import com.sh.metablog_prac.exception.Unauthorized;
import com.sh.metablog_prac.repository.SessionRepository;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.LoginRequest;
import com.sh.metablog_prac.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public Long signinV2(LoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        return user.getId();
    }
    
    // 토큰방식
    public String signin(LoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();// 명령과 조회가 같이 있는 느낌.. 값을 저장하면서 동시에 반환?
        return session.getAccessToken();
    }

    public void signup(Signup signup) {
//        boolean isDuplicated = userRepository.findAll()
//                                                .stream()
//                                                .anyMatch(user -> user.getEmail().equals(signup.getEmail()));
//
//        if (isDuplicated) {
//            throw new InvalidRequest("이미 존재하는 이메일입니다.");
//        }
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }
        userRepository.save(signup.toEntity());
    }
}
