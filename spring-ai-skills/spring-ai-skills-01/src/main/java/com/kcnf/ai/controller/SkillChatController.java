package com.kcnf.ai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class SkillChatController {


    @Autowired
    private ChatClient skillsAgent;


    @PostMapping("/chat")
    public String chat(@RequestBody String userInput) {
        // Agent 会自主决定是否以及如何调用技能
        return skillsAgent.prompt()
                .user(userInput)
                // 您的技能工具会在这里被自动调用
                .call()
                .content();
    }
}