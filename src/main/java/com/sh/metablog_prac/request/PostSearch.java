package com.sh.metablog_prac.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class PostSearch {

    private int page;
    private int size;

    @Builder
    public PostSearch(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
