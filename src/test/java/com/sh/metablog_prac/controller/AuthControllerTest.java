package com.sh.metablog_prac.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.metablog_prac.domain.Session;
import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.repository.SessionRepository;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.LoginRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void clean() {userRepository.deleteAll();}
    
    @Test
    @DisplayName("로그인 테스트")
    void test() throws Exception {
        User user = User.builder()
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password("1234") // Scrypt, Bcrypt 암호화 알고리즘 (원리 이해보단 컨셉정도만 이해해서 상황에 따라 선택가능할 정도로만 공부)
                .build();
        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(request);
        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Transactional //영속성컨텍스트를 유지시켜주지만 테스트를 오염시킴. 저장된 user를 레파지토리에서 다시 불러오는 의미도 없어짐.
    @Test
    @DisplayName("로그인 성공후 세션 1개 생성")
    void test2() throws Exception {
        // given
        User user = User.builder()
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        User findUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(findUser.getSessions()).hasSize(1);
    }


    @Test
    @DisplayName("로그인 성공후 세션 1개 생성")
    void test3() throws Exception {
        // given
        User user = User.builder()
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(("$.accessToken"), Matchers.notNullValue()))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("로그인 성공후 권한이 필요한 페이지에 접속한다.")
    void test4() throws Exception {
        // given
        User user = User.builder()
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .header("Authorization", session.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test5() throws Exception {
        // given
        User user = User.builder()
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .header("Authorization", session.getAccessToken() + "none"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }
}