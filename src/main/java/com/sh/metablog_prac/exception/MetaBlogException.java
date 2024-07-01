package com.sh.metablog_prac.exception;

import com.sh.metablog_prac.response.ErrorVO;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
abstract public class MetaBlogException extends RuntimeException {

    private final ErrorVO errorVO = new ErrorVO();

    public MetaBlogException(String message) {
        super(message);
    }

    public MetaBlogException(String message, Throwable cause) {
        super(message, cause);
    }

    abstract public String getStatusCode();

    public void addValidation(String fieldName, String message) {
        errorVO.setFieldName(fieldName);
        errorVO.setMessage(message);
    }
}
