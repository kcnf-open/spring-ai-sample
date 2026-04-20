package com.kcnf.ai.tool;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.function.Function;

@Component
public class MenuRecommendTool implements Function<MenuRecommendRequest, String> {

    @Override
    public String apply(MenuRecommendRequest taste) {
        if (taste == null || !StringUtils.hasLength(taste.getTaste())) {
            return "请告诉我你的口味偏好，比如：辣、甜、酸。";
        }

        if (taste.getTaste().contains("辣")) {
            return "🌶️ 推荐菜单：麻辣香锅、水煮牛肉、酸辣粉。";
        } else if (taste.getTaste().contains("甜")) {
            return "🍰 推荐菜单：糖醋排骨、拔丝地瓜、杨枝甘露。";
        } else if (taste.getTaste().contains("清淡")) {
            return "🥬 推荐菜单：白灼菜心、山药排骨汤、清蒸鲈鱼。";
        } else {
            return "🥢 推荐菜单：宫保鸡丁、番茄炒蛋、家常豆腐。";
        }
    }
}