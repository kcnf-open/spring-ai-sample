package com.kcnf.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MCP 客户端封装类，演示如何获取和使用工具
 */
@Slf4j
@Component
public class McpChatService {

    @Autowired
    private ChatClient chatClient;

    /**
     * 发送消息并获取响应（支持工具调用）
     */
    public String chat(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
}