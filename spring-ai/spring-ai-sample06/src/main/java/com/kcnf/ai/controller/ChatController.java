package com.kcnf.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatMemory chatMemory;

    /**
     * 智能对话接口（带角色设定和记忆功能）
     *
     * @param user 用户 ID（用于区分不同用户的会话）
     * @param role 系统角色设定（默认：A股预测专家）
     * @param msg 用户消息
     * @return AI 回复内容
     */
    @GetMapping("/gen/{user}")
    public String chatWithRole(
            @PathVariable("user") String user,
            @RequestParam(value = "role", defaultValue = "A股预测专家") String role,
            @RequestParam(value = "msg", defaultValue = "你好") String msg) {

        return chatClient.prompt()
                .system(systemSpec -> systemSpec.text("你是" + role + "。请以此角色与用户交流。"))
                .user(msg)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, user))
                .call()
                .content();
    }
}
