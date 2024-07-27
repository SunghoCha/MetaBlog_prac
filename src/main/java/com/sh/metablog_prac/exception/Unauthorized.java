package com.sh.metablog_prac.exception;

// 401 Unauthorized
public class Unauthorized extends MetaBlogException{

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    public Unauthorized(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getStatusCode() {
        return "401";
    }
}
