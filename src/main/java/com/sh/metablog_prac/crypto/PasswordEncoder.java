package com.sh.metablog_prac.crypto;

public interface PasswordEncoder {

    String encrypt(String rawPassword);

    boolean matches(String rawPassword, String encryptedPassword);
}
