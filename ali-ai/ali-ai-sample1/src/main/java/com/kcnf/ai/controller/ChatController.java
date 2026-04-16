package com.kcnf.ai.controller;


import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private ReactAgent reactAgent;

    @SneakyThrows
    @GetMapping("/ai/chat")
    public String chat(@RequestParam String message, @RequestParam(defaultValue = "default-user") String userId) {
        // 通过 threadId 隔离不同用户的会话，实现连续对话
        RunnableConfig config = RunnableConfig.builder()
                .threadId(userId)
                .build();

        // 调用 Agent 并返回结果
        return reactAgent.call(message, config).getText();
    }
}