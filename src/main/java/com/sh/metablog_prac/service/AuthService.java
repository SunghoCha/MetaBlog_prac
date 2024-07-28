package com.sh.metablog_prac.service;

import com.sh.metablog_prac.crypto.PasswordEncoder;
import com.sh.metablog_prac.crypto.ScryptPasswordEncoder;
import com.sh.metablog_prac.domain.Session;
import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.exception.AlreadyExistsEmailException;
import com.sh.metablog_prac.exception.InvalidSigninInformation;
import com.sh.metablog_prac.repository.SessionRepository;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.LoginRequest;
import com.sh.metablog_prac.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signinV2(LoginRequest request) {

        // 비밀번호를 암호화하기 때문에 바로 확인 불가능
//        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
//                .orElseThrow(InvalidSigninInformation::new);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidSigninInformation::new);
        
        
//        // 이 설정을 여러 곳에서 이렇게 만들어야하나? 최소한 이 컨트롤러에서라도 하나로 빼놓거나 아예 다른곳에서 불러오는게 나을지도
//        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
//                16,
//                8,
//                1,
//                32,
//                64);
        
        // Encoder 클래스 따로 생성
        boolean isMatched = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (isMatched) {
            return user.getId();
        } else {
            throw new InvalidSigninInformation();
        }
    }
    
    // 토큰방식 (비밀번호 암호화면서 더 이상 사용 불가능)
    public String signin(LoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();// 명령과 조회가 같이 있는 느낌.. 값을 저장하면서 동시에 반환?
        return session.getAccessToken();
    }

    public void signup(Signup signup) {

        // 방법1 (내가 한 임시방법)
        boolean isDuplicated = userRepository.findAll()
                                                .stream()
                                                .anyMatch(user -> user.getEmail().equals(signup.getEmail()));

        if (isDuplicated) {
            throw new AlreadyExistsEmailException();
        }

//        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
//        if (userOptional.isPresent()) {
//            throw new AlreadyExistsEmailException();
//        }

//        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
//                16,
//                8,
//                1,
//                32,
//                64);
//
//        String encryptedPassword = encoder.encode(signup.getPassword());

        String encodedPassword = passwordEncoder.encrypt(signup.getPassword());

        userRepository.save(signup.toEntity(encodedPassword));
    }
}
