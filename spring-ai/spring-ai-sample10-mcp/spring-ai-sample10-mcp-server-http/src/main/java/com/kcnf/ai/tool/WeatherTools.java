package com.kcnf.ai.tool;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WeatherTools {

    @Tool(name = "getWeather", description = "获取指定城市的天气信息")
    public String getWeather(
            @ToolParam(description = "城市名称", required = true) String city) {
        // 模拟返回天气数据
        log.info("[MCP Server] ✓✓✓ 检测到调用 getWeather 函数！✓✓✓");
        return String.format("%s: 晴天，温度25℃，湿度45%%", city);
    }

    @Tool(name = "calculate", description = "执行简单的数学计算")
    public double calculate(
            @ToolParam(description = "第一个数字", required = true) double a,
            @ToolParam(description = "第二个数字", required = true) double b,
            @ToolParam(description = "运算符 (add/subtract/multiply/divide)") String operator) {
        log.info("[MCP Server] ✓✓✓ 检测到调用 calculate 函数！✓✓✓");
        return switch (operator.toLowerCase()) {
            case "add" -> a + b;
            case "subtract" -> a - b;
            case "multiply" -> a * b;
            case "divide" -> a / b;
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }
}