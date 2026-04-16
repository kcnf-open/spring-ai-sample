package com.kcnf.ai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AgentController {

    @Autowired
    private ChatClient qwenClient;

    @Autowired
    private ChatClient zhipuaiClient;


    @GetMapping("/ai/qwen")
    public String chat(@RequestParam(value = "message", defaultValue = "你好") String message) {
        return qwenClient.prompt()
                .user(message)
                .call()
                .content();
    }


    @GetMapping("/ai/zhipuai")
    public String zhipuai(@RequestParam(value = "message", defaultValue = "你好") String message) {
        return zhipuaiClient.prompt()
                .user(message)
                .call()
                .content();
    }
}