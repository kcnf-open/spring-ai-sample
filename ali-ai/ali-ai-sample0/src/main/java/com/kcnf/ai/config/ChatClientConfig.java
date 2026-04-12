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
        return ChatClient.builder(chatModel)
                // 设置默认系统提示词，定义 AI 的基础人设
                .defaultSystem("你是一个专业、友好的AI助手，请用中文回答问题。")
                // 设置默认选项（可选）
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.7)   // 温度：控制回答的随机性，0=确定，1=创造性高
                        .topP(0.9)          // 核采样：控制词汇选择的多样性
                        .build())
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