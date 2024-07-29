package com.sh.metablog_prac.service;

import com.sh.metablog_prac.crypto.PasswordEncoder;
import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.exception.AlreadyExistsEmailException;
import com.sh.metablog_prac.exception.InvalidSigninInformation;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.Signup;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void clean() {
        userRepository.deleteAll();
    }

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
        assertTrue(passwordEncoder.matches("1234", users.get(0).getPassword()));
        /*
            비밀번호 테스트는 암호화 방식이 signup() 안에 들어있어서 테스트 힘듦
            이 기능을 추상화하여 인터페이스를 signup()이 인자로 받게하고 
            테스트 시에는 이 인터페이스를 구현한 간이암호화 메서드를 이용하여 쉽게 테스트하도록 바꿔주면 좋을듯
            
            여기선 encoder에 있는 rawPW와 encodedPW 비교하는 기능으로 해결
         */
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        // given
        String encryptedPassword = passwordEncoder.encrypt("1234");
        User user = User.builder()
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password(encryptedPassword)
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