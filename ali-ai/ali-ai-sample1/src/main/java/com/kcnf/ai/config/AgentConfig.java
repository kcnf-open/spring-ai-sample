package com.kcnf.ai.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.kcnf.ai.tool.MenuRecommendRequest;
import com.kcnf.ai.tool.MenuRecommendTool;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean
    public DashScopeChatModel chatModel() {
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(apiKey)
                .build();
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
    }


    @Bean
    public ToolCallback menuRecommendToolCallback(MenuRecommendTool menuTool) {
        return FunctionToolCallback.builder("recommend_menu", menuTool)
                .description("根据用户的口味偏好推荐合适的菜品")
                .inputType(MenuRecommendRequest.class)
                .build();
    }

    @Bean
    public ReactAgent reactAgent(DashScopeChatModel chatModel, ToolCallback menuRecommendToolCallback) {
        return ReactAgent.builder()
                .name("点餐助手")
                .model(chatModel)
                .tools(menuRecommendToolCallback)
                .systemPrompt("你是一个友好的餐厅点餐助手。你有推荐菜品的工具。当用户询问吃什么时，你需要调用工具来推荐。如果用户没有说明口味，请主动询问。")
                .saver(new MemorySaver())
                .build();
    }
}