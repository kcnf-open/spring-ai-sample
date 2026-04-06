package com.kcnf.ai.config;

import com.kcnf.ai.tool.MyTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Bean
    public MyTools myTools() {
        return new MyTools();
    }

    @Bean
    public ToolCallbackProvider myToolsProvider(MyTools myTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(myTools)
                .build();
    }
}
