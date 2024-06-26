package com.sh.metablog_prac.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;
}
