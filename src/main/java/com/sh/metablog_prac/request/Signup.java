package com.sh.metablog_prac.request;

import com.sh.metablog_prac.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Signup {

    private String account;
    private String name;
    private String email;
    private String password;

    @Builder
    public Signup(String account, String name, String email, String password) {
        this.account = account;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .account(account)
                .name(name)
                .email(email)
                .password(encodedPassword)
                .build();
    }
}
