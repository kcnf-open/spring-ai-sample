package com.kcnf.ai.controller;

import com.kcnf.ai.tool.WeatherTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ServerDebugController {

    @Autowired(required = false)
    private WeatherTools weatherTools;

    @GetMapping("/debug")
    public String debug() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MCP Server 调试信息 ===\n\n");
        sb.append("WeatherTools 是否注入: ").append(weatherTools != null ? "✅ 是" : "❌ 否").append("\n");

        if (weatherTools != null) {
            sb.append("WeatherTools 类型: ").append(weatherTools.getClass().getName()).append("\n");
        } else {
            sb.append("\n⚠️ WeatherTools 未被 Spring 扫描到！\n");
            sb.append("请检查:\n");
            sb.append("1. WeatherTools 类是否有 @Component 注解\n");
            sb.append("2. McpHttpServerApplication 是否能扫描到 tool 包\n");
        }

        return sb.toString();
    }
}
