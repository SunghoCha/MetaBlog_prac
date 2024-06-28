package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.domain.Post;
import com.sh.metablog_prac.repository.PostRepository;
import com.sh.metablog_prac.request.PostCreate;
import com.sh.metablog_prac.request.PostEdit;
import com.sh.metablog_prac.request.PostSearch;
import com.sh.metablog_prac.response.PostResponse;
import com.sh.metablog_prac.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

//    @PostMapping("/posts")
//    public Map<String, String> post(@Valid @RequestBody PostCreate postCreate, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            Map<String, String> errormap = new HashMap<>();
//            fieldErrors.forEach(fieldError -> {errormap.put(fieldError.getField(), fieldError.getDefaultMessage());});
//            return errormap;
//        }
//        log.info("postCreate = {}", postCreate);
//        return Map.of();
//    }

    @PostMapping("/posts")
    public void post(@Valid @RequestBody PostCreate postCreate) {
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
