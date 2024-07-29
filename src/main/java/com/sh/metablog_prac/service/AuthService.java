package com.sh.metablog_prac.service;

import com.sh.metablog_prac.crypto.PasswordEncoder;
import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.exception.AlreadyExistsEmailException;
import com.sh.metablog_prac.exception.InvalidSigninInformation;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(Signup signup) {

        boolean isDuplicated = userRepository.findAll()
                                                .stream()
                                                .anyMatch(user -> user.getEmail().equals(signup.getEmail()));

        if (isDuplicated) {
            throw new AlreadyExistsEmailException();
        }

        String encodedPassword = passwordEncoder.encrypt(signup.getPassword());

        userRepository.save(signup.toEntity(encodedPassword));
    }
}
