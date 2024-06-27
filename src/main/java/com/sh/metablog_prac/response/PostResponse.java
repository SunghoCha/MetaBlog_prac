package com.sh.metablog_prac.response;

import com.sh.metablog_prac.domain.Post;
import lombok.*;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    // 생성자 오버로딩
    // dto는 얼마든지 entity에 의존해도 상관없음 (반대는 안됨. entity는 순수하게 유지할 것)
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}
