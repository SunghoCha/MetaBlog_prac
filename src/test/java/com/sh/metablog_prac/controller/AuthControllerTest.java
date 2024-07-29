package com.sh.metablog_prac.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.metablog_prac.domain.User;
import com.sh.metablog_prac.repository.UserRepository;
import com.sh.metablog_prac.request.Signup;
import com.sh.metablog_prac.service.AuthService;
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

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {
        // given
        Signup signup = Signup.builder()
                .account("TJDGH3725")
                .name("sungho")
                .email("TJDGH3725@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(signup);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/public/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}