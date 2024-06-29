package com.sh.metablog_prac.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.metablog_prac.domain.Post;
import com.sh.metablog_prac.exception.PostNotFound;
import com.sh.metablog_prac.repository.PostRepository;
import com.sh.metablog_prac.request.PostCreate;
import com.sh.metablog_prac.request.PostEdit;
import com.sh.metablog_prac.service.PostService;
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

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostService postService;

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
//        PostCreate postCreate = PostCreate.builder()
//                .content("글 내용입니다.")
//                .build();

        PostCreate postCreate = new PostCreate("", "내용입니다.");

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation[0].message").value("타이틀을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test4() throws Exception {
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(post.getTitle().substring(0, 10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(post.getContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    public void test6() throws Exception {
        //given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title(i + "번째 글의 제목입니다.")
                        .content(i + "번째 글의 내용입니다.")
                        .build())
                .toList();
        postRepository.saveAll(posts);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&sort=id,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(posts.get(posts.size() - 1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("30번째 글의 제목입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value("30번째 글의 내용입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("(QDSL) 페이징 필드값을 전달하지 않으면 PostSearch의 디폴트값(page=1&size=10)으로 동작한다.")
    public void test7() throws Exception {
        //given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title(i + "번째 글의 제목입니다.")
                        .content(i + "번째 글의 내용입니다.")
                        .build())
                .toList();
        postRepository.saveAll(posts);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/postsWithQDSL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(posts.get(posts.size() - 1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("30번째 글의 제목입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value("30번째 글의 내용입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("(QDSL) 페이지를 0 또는 1로 요청하면 첫 페이지를 가져온다.")
    public void test8() throws Exception {
        //given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title(i + "번째 글의 제목입니다.")
                        .content(i + "번째 글의 내용입니다.")
                        .build())
                .toList();
        postRepository.saveAll(posts);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/postsWithQDSL?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(posts.get(posts.size() - 1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("30번째 글의 제목입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value("30번째 글의 내용입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 제목 수정")
    public void test9() throws Exception {
        //given
        Post post = Post.builder()
                .title("원본 제목")
                .content("원본 내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("수정된 제목")
                .content(post.getContent())
                .build();
        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId()) // PATCH /posts/{postId}
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void test10() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회시 PostNotFound 예외 발생")
    void test11() throws Exception {
        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(PostNotFound.MESSAGE))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정 시도시 PostNotFound 예외 발생")
    void test12() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(PostNotFound.MESSAGE))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 제목에 비속어는 포함될 수 없다")
    void test13() throws Exception{
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("비속어 포함된 제목")
                .content("내용입니다.")
                .build();
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postCreate)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("제목에 바보가 포함되면 InvalidRequest 예외 발생")
    void test14() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("바보가 포함된 제목")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(postCreate);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))

                .andDo(MockMvcResultHandlers.print());
    }
    /*
        Spring RestDocs 사용이유 :
        - 운영코드를 수정하지 않고 단순히 테스트코드만으로 문서 생성가능
        - 변경된 기능에 대해 최신문서 유지 가능
     */
}