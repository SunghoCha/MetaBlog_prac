package com.sh.metablog_prac.config;

import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.util.Base64;

//@Configuration
@Data
@ConfigurationProperties(prefix = "meta")
public class AppConfig {
    //public static final String KEY = "6qY8/IzWiBvGfNO38iPWWtApreThIcG8RBLHNOheZgg=";
    public static final SecretKey KEY = Jwts.SIG.HS256.key().build();

    public Hello hello;

    @Data
    public static class Hello {
        public String name;
        public String home;
        public String hobby;
        public int age;
    };
}
