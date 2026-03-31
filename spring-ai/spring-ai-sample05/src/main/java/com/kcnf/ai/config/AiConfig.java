package com.kcnf.ai.config;


import com.kcnf.ai.memory.DatabaseChatMemory;
import com.kcnf.ai.repository.JdbcChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.create(chatModel);
    }


    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository chatMemoryRepository) {
        return new DatabaseChatMemory(chatMemoryRepository);
    }

    @Bean
    public JdbcChatMemoryRepository jdbcChatMemoryRepository(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        return new JdbcChatMemoryRepository(jdbcTemplate);
    }


}