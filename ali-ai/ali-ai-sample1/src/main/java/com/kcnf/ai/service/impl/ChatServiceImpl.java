package com.kcnf.ai.service.impl;


import com.kcnf.ai.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {


    private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.defaultSystem("你是聪明的脱口秀小助手").build();
    }

    public Flux<String> chat(String question) {
        return chatClient.prompt(question).stream().content();
    }
}