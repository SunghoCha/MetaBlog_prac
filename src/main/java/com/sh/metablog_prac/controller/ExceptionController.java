package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.exception.InvalidRequest;
import com.sh.metablog_prac.exception.MetaBlogException;
import com.sh.metablog_prac.exception.PostNotFound;
import com.sh.metablog_prac.response.ErrorResponse;
import com.sh.metablog_prac.response.ErrorVO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class) // 스프링이 정의한 예외는 이렇게 따로 처리해주는게 좋음
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("methodArgumentNotValidException 작동");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        e.getFieldErrors().forEach(fieldError -> {
            errorResponse.addValidation(new ErrorVO(fieldError.getField(), getCustomErrorMessage(fieldError)));
            log.info("fieldError : {}", fieldError);
        });

        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(MetaBlogException.class) // 비지니스 로직에 관한 예외는 보통 정해진 규격이 있으므로 일괄적으로 처리가능
    public ResponseEntity<ErrorResponse> metaBlogException(MetaBlogException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getStatusCode())
                .message(e.getMessage())
                .build();
        errorResponse.addValidation(e.getErrorVO());

        return ResponseEntity.status(Integer.parseInt(e.getStatusCode())).body(errorResponse);
    }

    private String getCustomErrorMessage(FieldError fieldError) {
        String code = fieldError.getCode();
        String defaultMessage = fieldError.getDefaultMessage();
        Object[] args = {fieldError.getRejectedValue()}; // 자바 8이상 지원
        log.info("code : {}", code);
        log.info("args : {}", args);
        Locale locale = Locale.getDefault();
        if (code != null) {
            return messageSource.getMessage(code, args, defaultMessage, locale); // 왜 errors.properties 인식을 못하는거지
        }
        return defaultMessage;
    }
}
