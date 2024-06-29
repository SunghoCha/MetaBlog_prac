package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.repository.PostRepository;
import com.sh.metablog_prac.request.PostCreate;
import com.sh.metablog_prac.request.PostEdit;
import com.sh.metablog_prac.request.PostSearch;
import com.sh.metablog_prac.response.PostResponse;
import com.sh.metablog_prac.service.PostService;
import com.sh.metablog_prac.validator.PostCreateValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostCreateValidator postCreateValidator;

    @InitBinder("postCreate")
    public void initBinder(WebDataBinder webdataBinder) {
        log.info("Init Binder : {}", webdataBinder);
        webdataBinder.addValidators(postCreateValidator);
    }

    @PostMapping("/posts")
    public void post(@Valid @RequestBody PostCreate postCreate) {
        postCreate.validate();
        postService.write(postCreate);
    }

    /*
        /posts -> post 전체 조회(검색 + 페이징)
        /posts/{prostId} -> post 상세조회
     */

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        return postService.get(id);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(Pageable pageable) {
        log.info("pageSize, pageNum, offset, sort: {}, {}, {}, {}", pageable.getPageSize(), pageable.getPageNumber(), pageable.getOffset(), pageable.getSort());
        return postService.getList(pageable);
    }

    @GetMapping("/postsWithQDSL")
    public List<PostResponse> getListWithQDSL(@ModelAttribute PostSearch postSearch) {
        return postService.getListWithQDSL(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(postId, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
