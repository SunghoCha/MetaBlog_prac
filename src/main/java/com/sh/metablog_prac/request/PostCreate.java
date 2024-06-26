package com.sh.metablog_prac.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@ToString
@Getter @Setter
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;

    @Builder // 가독성 좋음, 값 생성에 대한 유연함(필요한 값만 받을 수 있음)
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
