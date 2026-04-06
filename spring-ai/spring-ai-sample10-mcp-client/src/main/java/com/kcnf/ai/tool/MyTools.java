package com.kcnf.ai.tool;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;


public class MyTools {

    @Tool(description = "计算两个整数的和")
    public int add(
            @ToolParam(description = "第一个数字") int a,
            @ToolParam(description = "第二个数字") int b) {
        return a + b;
    }

    @Tool(description = "获取当前时间的字符串，比如 14:30:25")
    public String getCurrentTime() {
        return java.time.LocalTime.now().toString();
    }
}