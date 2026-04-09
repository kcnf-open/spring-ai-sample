package com.kcnf.ai.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class ChatController {

    @Autowired
    private  ChatClient chatClient;
    @Autowired
    private  ToolCallbackProvider mcpTools;


    @GetMapping("/chat")
    public String chat(@RequestParam(defaultValue = "请使用 getWeather 工具获取上海城市的天气信息") String message) {
        log.info("=== 收到聊天请求 ===");
        log.info("用户消息: {}", message);
        log.info("mcpTools 可用: {}", mcpTools != null);

        var promptBuilder = chatClient.prompt()
                .user(message);

        if (mcpTools != null) {
            log.info("✓✓✓ 正在注册 MCP 工具回调 ✓✓✓");
            promptBuilder.toolCallbacks(mcpTools);
        } else {
            log.warn("⚠⚠⚠ MCP 工具不可用，将直接调用 LLM ⚠⚠⚠");
        }

        log.info("开始调用 LLM...");
        String response = promptBuilder
                .call()
                .content();

        log.info("LLM 响应: {}", response);
        return response;
    }

    @GetMapping("/check-tools")
    public String checkTools() {
        if (mcpTools == null) {
            return "❌ MCP 工具未加载";
        }

        try {
            var tools = mcpTools.getToolCallbacks();
            StringBuilder sb = new StringBuilder();
            sb.append("✅ MCP 工具已加载，共 ").append(tools.length).append(" 个工具:\n");
            for (var tool : tools) {
                sb.append("- ").append(tool.toString()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "❌ 检查工具时出错: " + e.getMessage();
        }
    }

    @GetMapping("/debug-mcp")
    public String debugMcp() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MCP 调试信息 ===\n\n");

        sb.append("1. mcpTools 对象: ").append(mcpTools == null ? "null" : mcpTools.getClass().getName()).append("\n");

        if (mcpTools != null) {
            try {
                var tools = mcpTools.getToolCallbacks();
                sb.append("2. 工具数量: ").append(tools.length).append("\n");

                if (tools.length == 0) {
                    sb.append("\n⚠️ 警告: 工具数量为 0，可能原因:\n");
                    sb.append("   - MCP Server 未启动或无法连接\n");
                    sb.append("   - MCP Server 没有注册任何工具\n");
                    sb.append("   - Client 和 Server 传输协议不匹配\n");
                    sb.append("   - Spring AI MCP Client 自动配置问题\n");
                }
            } catch (Exception e) {
                sb.append("2. 获取工具失败: ").append(e.getMessage()).append("\n");
            }
        }

        sb.append("\n请检查:\n");
        sb.append("1. MCP Server 是否在 http://localhost:8083/mcp 运行\n");
        sb.append("2. 访问 http://localhost:8083/mcp 查看 Server 状态\n");
        sb.append("3. 查看 MCP Server 启动日志，确认 WeatherTools 已注册\n");

        return sb.toString();
    }
}
