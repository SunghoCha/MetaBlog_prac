package com.sh.metablog_prac.service;

import com.sh.metablog_prac.domain.Post;
import com.sh.metablog_prac.exception.PostNotFound;
import com.sh.metablog_prac.repository.PostRepository;
import com.sh.metablog_prac.request.PostCreate;
import com.sh.metablog_prac.request.PostEdit;
import com.sh.metablog_prac.request.PostSearch;
import com.sh.metablog_prac.response.PostResponse;
import org.hibernate.dialect.TiDBDialect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    @DisplayName("글 1페이지 조회")
    public void test3() {
        //given
//        List<Post> posts = new ArrayList<>();
//        for (int i = 1; i <= 30; i++) {
//            posts.add(Post.builder()
//                            .title(i + "번째 제목입니다")
//                            .content(i + "번째 글의 내용입니다.")
//                            .build());
//        }
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title(i + "번째 글의 제목입니다.")
                        .content(i + "번째 글의 내용입니다.")
                        .build())
                .toList();

        postRepository.saveAll(requestPosts);

        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));

        //when
        List<PostResponse> postResponses = postService.getList(pageable);
        //then
        assertEquals(5L, postResponses.size());
        assertEquals("30번째 글의 제목입니다.", postResponses.get(0).getTitle());
        assertEquals("26번째 글의 제목입니다.", postResponses.get(4).getTitle());
    }

    @Test
    @DisplayName("글 1페이지 조회 with QueryDSL")
    public void test4() {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title(i + "번째 글의 제목입니다.")
                        .content(i + "번째 글의 내용입니다.")
                        .build())
                .toList();

        postRepository.saveAll(requestPosts);

        //PageRequest pageable = PageRequest.of(0, 5);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        //when
        List<PostResponse> postResponses = postService.getListWithQDSL(postSearch);
        //then
        assertEquals(10L, postResponses.size());
        assertEquals("30번째 글의 제목입니다.", postResponses.get(0).getTitle());
    }
    
    @Test
    @DisplayName("글 여러개 한 번에 저장")
    public void test5() {
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

    @Test
    @DisplayName("글 제목 수정")
    public void test6() {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("수정된foo1")
                .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals(postEdit.getTitle(), changedPost.getTitle());
    }

    @Test
    @DisplayName(" 값이 null로 전달되면 기존의 원본 값이 수정 후에도 유지된다.")
    public void test7() {
        //given
        Post post = Post.builder()
                .title("원본 제목")
                .content("원본 내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content(null)
                .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("원본 제목", changedPost.getTitle());
        assertEquals("원본 내용", changedPost.getContent());
    }

    @Test
    @DisplayName("editDefault 메서드를 이용한 수정 테스트")
    public void test8() {
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

        //when
        postService.editDefault(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("수정된 제목", changedPost.getTitle());
        assertEquals(post.getContent(), changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void test9() {
        //given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertEquals(0, postRepository.count());
    }
    
    @Test
    @DisplayName("게시글 조회 실패시 예외 반환")
    public void test10() {
        //given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);
        
        //expected
        assertThrows(PostNotFound.class, () -> postService.get(-1L));
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제 시도하면 PostNotFound 예외 발생")
    public void test11() {
        //given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        //expected
        assertThrows(PostNotFound.class, () -> postService.delete(-1L));
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정 시도하면 PostNotFound 예외 발생 ")
    public void test12() {
        //given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("수정 제목")
                .content("수정 내용")
                .build();
        //expected
         assertThrows(PostNotFound.class, () -> postService.edit(-1L, postEdit));


    }
}