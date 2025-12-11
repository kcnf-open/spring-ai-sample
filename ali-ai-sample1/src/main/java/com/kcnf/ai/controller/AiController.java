package com.kcnf.ai.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    @GetMapping("/chat")
    public String chat(String question) {
        return "hello world";
    }
}
