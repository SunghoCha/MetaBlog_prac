package com.sh.metablog_prac.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.metablog_prac.domain.Post;
import com.sh.metablog_prac.repository.PostRepository;
import com.sh.metablog_prac.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 등록 요청 테스트")
    void test2() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("글 제목입니다.")
                .content("글 내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 등록 요청시 title값은 필수다")
    void test3() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .content("글 내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation[0].fieldName").value("title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation[0].errorMessage").value("타이틀을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test4() throws Exception{
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("글 제목입니다.")
                .content("글 내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals(postCreate.getTitle(), post.getTitle());
        assertEquals(postCreate.getContent(), post.getContent());
    }
    
    @Test
    @DisplayName("글 1개 조회시 title 글자수는 앞글자부터 시작해서 최대 10글자이다")
    public void test5() throws Exception {
        //given
        Post post = Post.builder()
                .title("12345678910")
                .content("bar")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(post.getTitle().substring(0,10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(post.getContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    public void test6() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("foo2")
                .content("bar2")
                .build();
        postRepository.save(post2);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(post1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(post2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value(post1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].title").value(post2.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value(post1.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].content").value(post2.getContent()))
                .andDo(MockMvcResultHandlers.print());





    }
}