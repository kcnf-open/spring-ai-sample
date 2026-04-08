package com.kcnf.ai.controller;


import com.kcnf.ai.service.McpToolService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AIChatController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private McpToolService mcpToolService;

    @Autowired
    private ToolCallbackProvider toolCallbackProvider;

    @GetMapping("/gen/{user}")
    public String chatWithRole(
            @PathVariable("user") String user,
            @RequestParam(value = "role", defaultValue = "新闻专家") String role,
            @RequestParam(value = "msg", defaultValue = "你好") String msg) {

        return chatClient.prompt()
                .system(systemSpec -> systemSpec.text("你是" + role + "。请以此角色与用户交流。"))
                .user(msg)
                .tools()
                .call()
                .content();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "msg", defaultValue = "你好") String msg) {
        return mcpToolService.callMcpTool(msg);
    }

    @PostMapping("/chat")
    public String chatPost(@RequestBody ChatRequest request) {
        return mcpToolService.chatWithTools(
                request.getSystemPrompt() != null ? request.getSystemPrompt() : "你是一个有用的助手",
                request.getMessage()
        );
    }

    @GetMapping("/test-tools")
    public String testTools() {
        String question = "请帮我计算 25 加 37 等于多少？";
        return mcpToolService.callMcpTool(question);
    }

    @GetMapping("/test-time")
    public String testTime() {
        String question = "现在几点了？";
        return mcpToolService.callMcpTool(question);
    }


    @GetMapping("/mcp/direct/{toolName}")
    public String callMcpDirectly(
            @PathVariable("toolName") String toolName,
            @RequestParam Map<String, Object> arguments) {
        return mcpToolService.callMcpToolDirectly(toolName, arguments);
    }


    @GetMapping("/mcp/health")
    public Map<String, Object> checkMcpHealth() {
        Map<String, Object> health = new java.util.HashMap<>();

        if (toolCallbackProvider == null) {
            health.put("status", "ERROR");
            health.put("message", "ToolCallbackProvider 未注入");
            health.put("toolCount", 0);
            return health;
        }

        try {
            org.springframework.ai.tool.ToolCallback[] tools = toolCallbackProvider.getToolCallbacks();
            health.put("status", "OK");
            health.put("toolCount", tools.length);

            java.util.List<Map<String, String>> toolList = new java.util.ArrayList<>();
            for (org.springframework.ai.tool.ToolCallback tool : tools) {
                Map<String, String> toolInfo = new java.util.HashMap<>();
                toolInfo.put("name", tool.getToolDefinition().name());
                toolInfo.put("description", tool.getToolDefinition().description());
                toolList.add(toolInfo);
            }
            health.put("tools", toolList);
        } catch (Exception e) {
            health.put("status", "ERROR");
            health.put("message", e.getMessage());
            health.put("toolCount", 0);
        }

        return health;
    }


    @Setter
    @Getter
    public static class ChatRequest {
        private String message;
        private String systemPrompt;
    }
}
