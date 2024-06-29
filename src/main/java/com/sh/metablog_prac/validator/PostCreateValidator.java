package com.sh.metablog_prac.validator;

import com.sh.metablog_prac.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class PostCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PostCreate.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PostCreate postCreate = (PostCreate) target;

        if (postCreate.getTitle().contains("비속어")) {
            log.info("비속어 필터 동작");
            errors.rejectValue("title", "PostTitle", "타이틀에 비속어 포함");
        }
    }
}
