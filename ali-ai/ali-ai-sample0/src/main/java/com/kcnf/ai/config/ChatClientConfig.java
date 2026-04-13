package com.kcnf.ai.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    /**
     * 创建 ChatClient Bean
     * ChatClient 是 Spring AI 推荐的现代调用方式，支持链式编程和插件机制
     */
    @Bean
    public ChatClient chatClient(DashScopeChatModel chatModel) {
        ChatOptions chatOptions =  ChatOptions.builder().temperature(0.9).build();
        return ChatClient.builder(chatModel)
                // 设置默认系统提示词，定义 AI 的基础人设
                .defaultSystem("你是一个专业新闻播报助手，请用中文回答问题。")
                // 设置默认选项（可选）
                .defaultOptions(chatOptions)
                .build();
    }

    /**
     * 创建 ChatClient.Builder Bean，供需要动态构建 ChatClient 的场景使用
     */
    @Bean
    public ChatClient.Builder chatClientBuilder(DashScopeChatModel chatModel) {

        return ChatClient.builder(chatModel);
    }
}