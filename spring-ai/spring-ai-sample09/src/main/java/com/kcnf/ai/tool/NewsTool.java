package com.kcnf.ai.tool;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.kcnf.ai.dal.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class NewsTool {

    @Autowired
    private NewsApiClient newsApiClient;

    @Tool(description = "当用户询问新闻、时事、热点、最新资讯等相关内容时，必须调用此工具。" +
            "可以搜索特定关键词的新闻，也可以获取最新新闻。" +
            "适用场景：用户提到'新闻'、'资讯'、'报道'、'热点'、'动态'等关键词。")
    public String searchNews(Request request) {
        log.info(">>> [工具调用] NewsTool 被触发");
        log.info(">>> 搜索关键词: {}", request.keyword);

        try {
            List<News> newsList = newsApiClient.searchByKeyword(request.keyword);
            if (newsList == null || newsList.isEmpty()) {
                log.info("<<< 未找到相关新闻");
                return "未找到相关关键词的新闻。";
            }

            log.info("<<< 找到 {} 条新闻", newsList.size());
            StringBuilder result = new StringBuilder();
            result.append("找到 ").append(newsList.size()).append(" 条新闻：\n\n");

            for (int i = 0; i < Math.min(newsList.size(), 10); i++) {
                News news = newsList.get(i);
                result.append(i + 1).append(". [").append(news.getDate()).append("] ")
                        .append(news.getTitle()).append("\n")
                        .append("   内容: ").append(news.getContent()).append("\n\n");
            }
            return result.toString();
        } catch (Exception e) {
            log.error("<<< 搜索新闻时出错", e);
            return "搜索新闻时出错: " + e.getMessage();
        }
    }

    @JsonClassDescription("搜索新闻文章的请求")
    public record Request(
            @JsonProperty(value = "keyword", required = false)
            @JsonPropertyDescription("在新闻文章中搜索的关键词。留空则获取所有最近的新闻。")
            String keyword
    ) {}
}
