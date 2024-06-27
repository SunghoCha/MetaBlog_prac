package com.sh.metablog_prac.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.metablog_prac.domain.Post;
import com.sh.metablog_prac.repository.PostRepository;
import com.sh.metablog_prac.request.PostCreate;
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

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

//    @Test
//    @DisplayName("글 여러개 조회")
//    public void test6() throws Exception {
//        //given
//        List<Post> posts = IntStream.range(1, 31)
//                .mapToObj(i -> Post.builder()
//                        .title(i + "번째 글의 제목입니다.")
//                        .content(i + "번째 글의 내용입니다.")
//                        .build())
//                .toList();
//        postRepository.saveAll(posts);
//        //expected
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&sort=id,desc")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", is(5)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(30))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("30번째 글의 제목입니다."))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value("30번째 글의 내용입니다."))
//                .andDo(MockMvcResultHandlers.print());
//    }
}