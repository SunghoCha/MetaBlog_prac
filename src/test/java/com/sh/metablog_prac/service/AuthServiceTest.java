package com.sh.metablog_prac.service;

import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.exception.AlreadyExistsEmailException;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.Signup;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
        Signup signup = Signup.builder()
                .account("TJDGH3725")
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();

        // when
        authService.signup(signup);
        List<User> users = userRepository.findAll();
        // then
        assertThat(users).hasSize(1)
                .extracting("account", "name", "email", "password")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("TJDGH3725", "sungho", "TJDGH3725@gmail.com", "1234")
                );
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        // given
        User user = User.builder()
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Signup signup = Signup.builder()
                .account("TJDGH3725")
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        // when
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
    }
}