package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.request.PostCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @GetMapping("/posts")
    public String get() {return "Hello World";}

    @PostMapping("/posts")
    public Map<String, String> post(@Valid @RequestBody PostCreate postCreate, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> errormap = new HashMap<>();
            fieldErrors.forEach(fieldError -> {errormap.put(fieldError.getField(), fieldError.getDefaultMessage());});
            return errormap;
        }
        log.info("postCreate = {}", postCreate);
        return Map.of();
    }
}
