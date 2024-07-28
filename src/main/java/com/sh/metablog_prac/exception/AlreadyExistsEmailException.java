package com.sh.metablog_prac.exception;

public class AlreadyExistsEmailException extends MetaBlogException{

    public static final String MESSAGE = "이미 가입된 이메일입니다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    public AlreadyExistsEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getStatusCode() {
        return "400";
    }

}
