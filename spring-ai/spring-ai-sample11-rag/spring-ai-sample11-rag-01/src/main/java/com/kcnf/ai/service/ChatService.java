package com.kcnf.ai.service;


import com.kcnf.ai.tool.NewsTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatMemory chatMemory;

    @Autowired
    private NewsTool newsTool;

    public String chat(String conversationId, String userMessage) {
        log.info("========== 开始聊天处理 ==========");
        log.info("会话ID: {}", conversationId);
        log.info("用户消息: {}", userMessage);

        try {
            log.info("准备调用 LLM，启用记忆功能和工具调用");
            String response = chatClient.prompt()
                    .system("你是一个智能助手。当用户询问新闻、资讯、时事等内容时，你必须使用 searchNews 工具来搜索相关新闻。")
                    .tools(newsTool)
                    .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                    .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                    .user(userMessage)
                    .call()
                    .content();
            log.info("LLM 调用成功，收到响应");
            log.debug("响应内容: {}", response);
            log.info("========== 聊天处理完成 ==========");

            return response;
        } catch (Exception e) {
            log.error("聊天处理失败", e);
            throw e;
        }
    }
}
