package com.sh.metablog_prac.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sh.metablog_prac.domain.Post;

import com.sh.metablog_prac.domain.QPost;
import com.sh.metablog_prac.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements  PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getListWithQDSL(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset((long) (postSearch.getPage() - 1) * postSearch.getSize())
                .orderBy(QPost.post.id.desc())
                .fetch();

    }
}
