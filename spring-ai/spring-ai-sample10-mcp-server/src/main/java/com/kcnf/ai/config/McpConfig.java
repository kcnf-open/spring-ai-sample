package com.kcnf.ai.config;

import com.kcnf.ai.tool.MyServerTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Bean
    public MyServerTools myTools() {
        return new MyServerTools();
    }

    @Bean
    public ToolCallbackProvider myToolsProvider(MyServerTools myTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(myTools)
                .build();
    }



}
