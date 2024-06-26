package com.sh.metablog_prac.controller;

import com.sh.metablog_prac.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @GetMapping("/posts")
    public String get() {return "Hello World";}

    @PostMapping("/posts")
    public String post(@RequestBody PostCreate postCreate) {
        log.info("postCreate = {}", postCreate);
        return "Hello World";
    }
}
