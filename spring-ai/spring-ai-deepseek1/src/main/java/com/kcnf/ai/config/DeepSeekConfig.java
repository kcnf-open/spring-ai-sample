//package com.kcnf.ai.config;
//
//
//import org.springframework.ai.deepseek.DeepSeekChatModel;
//import org.springframework.ai.deepseek.DeepSeekChatOptions;
//import org.springframework.ai.deepseek.api.DeepSeekApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestClient;
//
//@Configuration
//public class DeepSeekConfig {
//
//
//    @Bean
//    public DeepSeekChatModel deepSeekChatModel() {
//        // 1. 构建HTTP客户端（可自定义超时等）
//        RestClient.Builder restClientBuilder = RestClient.builder()
//                .baseUrl("https://api.deepseek.com")
//                .defaultHeader("Authorization", "Bearer " + System.getenv("DEEPSEEK_API_KEY"));
//
//        // 2. 创建DeepSeekApi客户端
//        DeepSeekApi deepSeekApi = new DeepSeekApi(restClientBuilder);
//
//        // 3. 设置模型参数
//        DeepSeekChatOptions options = DeepSeekChatOptions.builder()
//                .model("deepseek-chat")
//                .temperature(0.7)
//                .build();
//
//        // 4. 实例化ChatModel
//        return new DeepSeekChatModel(deepSeekApi, options);
//    }
//}