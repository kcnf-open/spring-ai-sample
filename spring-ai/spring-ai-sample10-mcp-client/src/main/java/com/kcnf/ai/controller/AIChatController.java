package com.kcnf.ai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AIChatController {

    @Autowired
    private ChatClient chatClient;


    @GetMapping("/gen/{user}")
    public String chatWithRole(
            @PathVariable("user") String user,
            @RequestParam(value = "role", defaultValue = "新闻专家") String role,
            @RequestParam(value = "msg", defaultValue = "你好") String msg) {

        return chatClient.prompt()
                .system(systemSpec -> systemSpec.text("你是" + role + "。请以此角色与用户交流。"))
                .user(msg)
                .tools()
                .call()
                .content();
    }

}
