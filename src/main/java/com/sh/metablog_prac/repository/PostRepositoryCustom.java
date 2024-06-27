package com.sh.metablog_prac.repository;

import com.sh.metablog_prac.domain.Post;
import com.sh.metablog_prac.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getListWithQDSL(PostSearch postSearch);
}
