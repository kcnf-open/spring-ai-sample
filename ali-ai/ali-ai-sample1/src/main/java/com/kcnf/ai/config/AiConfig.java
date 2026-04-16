package com.kcnf.ai.config;


import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${ali.ai.qwen.api-key}")
    private String qwenApiKey;

    @Value("${ali.ai.qwen.model:qwen-plus}")
    private String qwenModel;

    @Value("${ali.ai.zhipuai.api-key}")
    private String zhipuaiApiKey;

    @Value("${ali.ai.zhipuai.model:glm-4}")
    private String zhipuaiModel;

    @Bean(name = "qwen")
    public ChatModel qwen() {
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(qwenApiKey).build())
                .defaultOptions(DashScopeChatOptions.builder().model(qwenModel).build())
                .build();
    }

    @Bean(name = "zhipuai")
    public ChatModel zhipuai() {
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(zhipuaiApiKey).build())
                .defaultOptions(DashScopeChatOptions.builder().model(zhipuaiModel).build())
                .build();
    }


    @Bean
    public ChatClient qwenClient(DashScopeChatModel qwen) {
        ChatOptions chatOptions =  ChatOptions.builder().temperature(0.9).build();
        return ChatClient.builder(qwen)
                // 设置默认系统提示词，定义 AI 的基础人设
                .defaultSystem("你是一个专业新闻播报助手，请用中文回答问题。")
                // 设置默认选项（可选）
                .defaultOptions(chatOptions)
                .build();
    }


    @Bean
    public ChatClient zhipuaiClient(DashScopeChatModel zhipuai) {
        ChatOptions chatOptions =  ChatOptions.builder().temperature(0.9).build();
        return ChatClient.builder(zhipuai)
                // 设置默认系统提示词，定义 AI 的基础人设
                .defaultSystem("你是一个专业新闻播报助手，请用中文回答问题。")
                // 设置默认选项（可选）
                .defaultOptions(chatOptions)
                .build();
    }

}
