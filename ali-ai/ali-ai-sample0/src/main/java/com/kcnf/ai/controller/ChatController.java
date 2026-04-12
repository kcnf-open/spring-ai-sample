package com.kcnf.ai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    // 通过构造器注入 ChatClient
    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    // 1. 普通同步调用
    @GetMapping("/ai/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "你好") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    // 2. 流式异步调用 (Server-Sent Events)
    @GetMapping(value = "/ai/chat/stream", produces = "text/plain; charset=utf-8")
    public Flux<String> streamChat(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}