package com.kcnf.ai.tool;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MyServerTools {

    @Tool(description = "计算两个整数的和")
    public int add(
            @ToolParam(description = "第一个数字") int a,
            @ToolParam(description = "第二个数字") int b) {
        log.info("╔══════════════════════════════════════════════════════════╗");
        log.info("║         [MCP Server] Tool 被调用                         ║");
        log.info("╚══════════════════════════════════════════════════════════╝");
        log.info("[MCP Server] ✓✓✓ 检测到调用 add 函数！✓✓✓");
        log.info("[MCP Server] 参数 a = {}", a);
        log.info("[MCP Server] 参数 b = {}", b);
        int result = a + b;
        log.info("[MCP Server] 计算结果: {} + {} = {}", a, b, result);
        log.info("═══════════════════════════════════════════════════════════");
        return result;
    }

    @Tool(description = "获取当前时间的字符串，比如 14:30:25")
    public String getCurrentTime() {
        log.info("╔══════════════════════════════════════════════════════════╗");
        log.info("║         [MCP Server] Tool 被调用                         ║");
        log.info("╚══════════════════════════════════════════════════════════╝");
        log.info("[MCP Server] ✓✓✓ 检测到调用 getCurrentTime 函数！✓✓✓");
        String time = java.time.LocalTime.now().toString();
        log.info("[MCP Server] 当前时间: {}", time);
        log.info("═══════════════════════════════════════════════════════════");
        return time;
    }
}
