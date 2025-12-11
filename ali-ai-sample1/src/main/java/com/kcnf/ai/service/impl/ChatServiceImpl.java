package com.kcnf.ai.service.impl;


import com.kcnf.ai.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;

public class ChatServiceImpl implements ChatService {


    private ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.defaultSystem("").build();
    }

    public String chat(String question) {

        return "";
    }
}