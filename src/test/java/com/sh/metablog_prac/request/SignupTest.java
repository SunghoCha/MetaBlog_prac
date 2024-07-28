package com.sh.metablog_prac.request;

import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.service.AuthService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SignupTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
        Signup signup = Signup.builder()
                .account("tjdgh3725")
                .name("Meta")
                .email("tjdgh3725@gmail.com")
                .password("1234")
                .build();
        // when
        authService.signup(signup);
        List<User> users = userRepository.findAll();
        // then
        assertThat(users).hasSize(1)
                .extracting("account", "name", "email", "password")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("tjdgh3725", "Meta", "tjdgh3725@gmail.com", "1234")
                );
    }
}