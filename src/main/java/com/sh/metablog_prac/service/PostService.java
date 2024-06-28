package com.sh.metablog_prac.service;


import com.sh.metablog_prac.domain.Post;
import com.sh.metablog_prac.domain.PostEditor;
import com.sh.metablog_prac.repository.PostRepository;
import com.sh.metablog_prac.repository.PostRepositoryCustom;
import com.sh.metablog_prac.request.PostCreate;
import com.sh.metablog_prac.request.PostEdit;
import com.sh.metablog_prac.request.PostSearch;
import com.sh.metablog_prac.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        // postCreate -> Post
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    //모든 글을 다 조회하게 되면 비용 부담, DB 문제 발생 -> 페이징 필요
    public List<PostResponse> getList(Pageable pageable) {
        //Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));

        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(toList()); // findAll() : 반환 데이터가 없더라도 빈 리스트를 반환해서 NPE가 발생하지 않으므로 굳이 Optional로 반환하지 않게 설계된듯
    }

    public List<PostResponse> getListWithQDSL(PostSearch postSearch) {
        return postRepository.getListWithQDSL(postSearch).stream()
                .map(PostResponse::new)
                .collect(toList());
    }

    public void edit(@RequestParam Long id, @ModelAttribute PostEdit postEdit) {
        Post post = postRepository.findById(id) // 트랜잭션 안에서 영속성 상태로 가져옴
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }
}
