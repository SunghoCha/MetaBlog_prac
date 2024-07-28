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
                .extracting("account", "name", "email")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("TJDGH3725", "sungho", "TJDGH3725@gmail.com")
                );
        /*
            비밀번호 테스트는 암호화 방식이 signup() 안에 들어있어서 테스트 힘듦
            이 기능을 추상화하여 인터페이스를 signup()이 인자로 받게하고 
            테스트 시에는 이 인터페이스를 구현한 간이암호화 메서드를 이용하여 쉽게 테스트하도록 바꿔주면 좋을듯
         */
        
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