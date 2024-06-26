package com.sh.metablog_prac.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
   <예시>
   {
        "code": "400"
        "message": "잘못된 요청입니다.",
        "validation": {
            "title": "값을 입력해주세요"
         }
    }
 */
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final List<ValidationTuple> validation = new ArrayList<>();

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String field, String defaultMessage) {
        this.validation.add(new ValidationTuple(field, defaultMessage));
    }

    @Getter
    @RequiredArgsConstructor
    private class ValidationTuple {
        private final String fieldName;
        private final String errorMessage;
    }
}
