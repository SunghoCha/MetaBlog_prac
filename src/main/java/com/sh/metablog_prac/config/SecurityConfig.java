package com.sh.metablog_prac.config;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import javax.crypto.SecretKey;

@Configuration
public class SecurityConfig {
    //public static final String KEY = "6qY8/IzWiBvGfNO38iPWWtApreThIcG8RBLHNOheZgg=";
    public static final SecretKey KEY = Jwts.SIG.HS256.key().build();
}
