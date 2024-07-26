package com.sh.metablog_prac.exception;

import java.lang.reflect.Field;
import java.util.Stack;

public class InvalidSigninInformation extends MetaBlogException{

    private static final String MESSAGE = "아이디, 비밀번호가 올바르지 않습니다.";

    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    public InvalidSigninInformation(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getStatusCode() {
        return "400";
    }
}
