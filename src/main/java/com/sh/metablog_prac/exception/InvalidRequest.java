package com.sh.metablog_prac.exception;

// 400 Bad Request
public class InvalidRequest extends MetaBlogException {

    public static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
         super(MESSAGE);
    }

    public InvalidRequest(String message) {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public String getStatusCode() {
        return "400";
    }
}
