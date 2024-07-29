package com.sh.metablog_prac.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String account;

    private String name;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    @Builder
    public User(String account, String name, String email, String password, LocalDateTime createdAt) {
        this.account = account;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }
}
