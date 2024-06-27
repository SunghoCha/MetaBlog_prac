package com.sh.metablog_prac.service;

import com.sh.metablog_prac.domain.Post;
import com.sh.metablog_prac.repository.PostRepository;
import com.sh.metablog_prac.request.PostCreate;
import com.sh.metablog_prac.response.PostResponse;
import org.hibernate.dialect.TiDBDialect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        //when
        postService.write(postCreate);
        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals(postCreate.getTitle(), post.getTitle());
        assertEquals(postCreate.getContent(), post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    public void test2() {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);
        //when
        PostResponse postResponse = postService.get(post.getId());
        //then
        assertNotNull(post);
        assertEquals(post.getTitle(), postResponse.getTitle());
        assertEquals(post.getContent(), postResponse.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    public void test3() {
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
        //when
        List<PostResponse> postResponses = postService.getList();
        //then
        assertEquals(2L, postResponses.size());
    }
    
    @Test
    @DisplayName("글 여러개 한 번에 저장")
    public void test4() {
        //given
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post.builder()
                        .title("foo2")
                        .content("bar2")
                        .build()
        ));
        //when
        List<Post> posts = postRepository.findAll();
        //then
        assertEquals(2L, posts.size());
    }
}