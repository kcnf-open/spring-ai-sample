package com.kcnf.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Service
public class McpToolService {

    @Autowired
    private ChatClient chatClient;

    @Autowired(required = false)
    private ToolCallbackProvider toolCallbackProvider;

    public String callMcpTool(String question) {
        log.info("╔══════════════════════════════════════════════════════════╗");
        log.info("║              MCP Tool 调用链路追踪                       ║");
        log.info("╚══════════════════════════════════════════════════════════╝");
        log.info("[步骤1] 接收用户问题: {}", question);
        log.info("[步骤2] 准备调用 ChatClient，启用 tools() 功能");
        log.info("[步骤3] 调用 LLM（智谱AI），LLM 将决定是否调用 MCP Server 的工具");

        try {
            long startTime = System.currentTimeMillis();

            String result = chatClient.prompt()
                    .user(question)
                    .tools()
                    .call()
                    .content();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("[步骤4] 收到 LLM 响应，耗时: {} ms", duration);
            log.info("[步骤5] LLM 响应内容: {}", result);

            if (result.contains("62") || result.contains("add")) {
                log.info("[MCP验证] ✓ 检测到响应中包含计算结果，说明 MCP Server 的 add 工具被调用了！");
            }
            if (result.matches(".*\\d{2}:\\d{2}:\\d{2}.*")) {
                log.info("[MCP验证] ✓ 检测到响应中包含时间格式，说明 MCP Server 的 getCurrentTime 工具被调用了！");
            }

            log.info("╔══════════════════════════════════════════════════════════╗");
            log.info("║              MCP Tool 调用完成                           ║");
            log.info("╚══════════════════════════════════════════════════════════╝");

            return result;
        } catch (Exception e) {
            log.error("[异常] MCP Tool 调用失败: {}", e.getMessage(), e);
            log.error("[异常详情] 异常类型: {}", e.getClass().getName());
            if (e.getCause() != null) {
                log.error("[异常原因] {}", e.getCause().getMessage());
            }
            throw e;
        }
    }

    public String chatWithTools(String systemPrompt, String userMessage) {
        log.info("╔══════════════════════════════════════════════════════════╗");
        log.info("║         MCP Tool 调用链路追踪（带系统提示）               ║");
        log.info("╚══════════════════════════════════════════════════════════╝");
        log.info("[步骤1] 系统提示: {}", systemPrompt);
        log.info("[步骤2] 用户消息: {}", userMessage);
        log.info("[步骤3] 准备调用 ChatClient，启用 tools() 功能");
        log.info("[步骤4] 调用 LLM（智谱AI），LLM 将决定是否调用 MCP Server 的工具");

        try {
            long startTime = System.currentTimeMillis();

            String result = chatClient.prompt()
                    .system(systemSpec -> systemSpec.text(systemPrompt))
                    .user(userMessage)
                    .tools()
                    .call()
                    .content();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("[步骤5] 收到 LLM 响应，耗时: {} ms", duration);
            log.info("[步骤6] LLM 响应内容: {}", result);

            if (result.contains("62") || result.contains("add")) {
                log.info("[MCP验证] ✓ 检测到响应中包含计算结果，说明 MCP Server 的 add 工具被调用了！");
            }
            if (result.matches(".*\\d{2}:\\d{2}:\\d{2}.*")) {
                log.info("[MCP验证] ✓ 检测到响应中包含时间格式，说明 MCP Server 的 getCurrentTime 工具被调用了！");
            }

            log.info("╔══════════════════════════════════════════════════════════╗");
            log.info("║              MCP Tool 调用完成                           ║");
            log.info("╚══════════════════════════════════════════════════════════╝");

            return result;
        } catch (Exception e) {
            log.error("[异常] MCP Tool 调用失败: {}", e.getMessage(), e);
            log.error("[异常详情] 异常类型: {}", e.getClass().getName());
            if (e.getCause() != null) {
                log.error("[异常原因] {}", e.getCause().getMessage());
            }
            throw e;
        }
    }


    public String callMcpToolDirectly(String toolName, Map<String, Object> arguments) {
        log.info("╔══════════════════════════════════════════════════════════╗");
        log.info("║         直接调用 MCP Server（不经过 LLM）                ║");
        log.info("╚══════════════════════════════════════════════════════════╝");
        log.info("[步骤1] 目标工具: {}", toolName);
        log.info("[步骤2] 调用参数: {}", arguments);

        if (toolCallbackProvider == null) {
            log.error("[错误] ToolCallbackProvider 未注入，无法调用 MCP 工具！");
            throw new IllegalStateException("MCP 工具提供者未配置");
        }

        try {
            long startTime = System.currentTimeMillis();

            ToolCallback[] tools = null;
            int maxRetries = 5;
            int retryCount = 0;

            while (retryCount < maxRetries) {
                tools = toolCallbackProvider.getToolCallbacks();
                log.info("[步骤3] 第 {} 次尝试，从 MCP Server 获取到 {} 个可用工具", retryCount + 1, tools.length);

                if (tools.length > 0) {
                    log.info("[成功] MCP Server 已就绪！");
                    break;
                }

                retryCount++;
                if (retryCount < maxRetries) {
                    log.warn("[警告] MCP Server 尚未就绪，等待 2 秒后重试... ({}/{})", retryCount, maxRetries);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException("等待 MCP Server 就绪时被中断", ie);
                    }
                }
            }

            if (tools == null || tools.length == 0) {
                log.error("[严重错误] MCP Server 没有返回任何工具（已重试 {} 次）！", maxRetries);

                throw new IllegalStateException(
                        "MCP Server 未就绪，可用工具数量为 0。" +
                                "\n可能原因：" +
                                "\n1. MCP Server 正在启动中，请稍后重试（等待 5-10 秒）" +
                                "\n2. MCP Server 启动失败，请检查 server 端日志" +
                                "\n3. 配置文件中的 JAR 路径不正确"
                );
            }

            log.info("[调试] 可用工具列表:");
            for (int i = 0; i < tools.length; i++) {
                String name = tools[i].getToolDefinition().name();
                String desc = tools[i].getToolDefinition().description();
                log.info("  [{}] 工具名: {}, 描述: {}", i + 1, name, desc);
            }

            ToolCallback[] finalTools = tools;
            ToolCallback targetTool = Arrays.stream(tools)
                    .filter(tool -> tool.getToolDefinition().name().equals(toolName))
                    .findFirst()
                    .orElseThrow(() -> {
                        String availableTools = Arrays.stream(finalTools)
                                .map(t -> t.getToolDefinition().name())
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("无");
                        log.error("[错误] 未找到工具 '{}', 可用工具: {}", toolName, availableTools);
                        return new IllegalArgumentException("未找到工具: " + toolName + "。可用工具: [" + availableTools + "]");
                    });

            log.info("[步骤4] 找到目标工具: {}", targetTool.getToolDefinition().name());
            log.info("[步骤5] 工具描述: {}", targetTool.getToolDefinition().description());
            log.info("[步骤6] 正在调用 MCP Server 执行工具...");

            String result = targetTool.call(arguments.toString());

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("[步骤7] MCP Server 执行完成，耗时: {} ms", duration);
            log.info("[步骤8] 执行结果: {}", result);

            log.info("╔══════════════════════════════════════════════════════════╗");
            log.info("║         MCP Server 直接调用完成                          ║");
            log.info("╚══════════════════════════════════════════════════════════╝");

            return result;
        } catch (Exception e) {
            log.error("[异常] 直接调用 MCP 工具失败: {}", e.getMessage(), e);
            throw e;
        }
    }


}
