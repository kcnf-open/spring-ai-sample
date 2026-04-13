package com.kcnf.ai.config;


import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class AgentConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel, ToolCallback skillsTool,
                                 ToolCallbackProvider fileSystemTools,
                                 ToolCallbackProvider shellTools) {
        return ChatClient.builder(chatModel)
                .defaultTools(skillsTool)
                .defaultToolCallbacks(fileSystemTools, shellTools)
                .build();
    }

    @Bean
    public ToolCallback skillsTool(@Value("${skills.root-path:classpath:skills}") List<Resource> skillsResources) {
        return SkillsTool.builder()
                .addSkillsResources(skillsResources)
                .build();
    }
}
