package com.kcnf.ai.job;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcnf.ai.dal.News;
import com.kcnf.ai.dal.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NewsFetchService {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${news.api.url}")
    private String apiUrl;

    @Autowired
    private NewsRepository newsRepository;

    @Transactional
    public void fetchAndStore() {
        log.info("开始从 60s API 拉取新闻...");
        try {
            Request request = new Request.Builder().url(apiUrl).get().build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("API 请求失败: {}", response);
                    return;
                }
                assert response.body() != null;
                String json = response.body().string();
                log.debug("API 响应: {}", json);

                JsonNode root = objectMapper.readTree(json);

                int code = root.path("code").asInt(-1);
                if (code != 200) {
                    String message = root.path("message").asText("未知错误");
                    log.error("API 返回错误码: {}, 消息: {}", code, message);
                    return;
                }

                JsonNode dataNode = root.path("data");
                if (dataNode.isMissingNode() || dataNode.isNull()) {
                    log.error("API 返回的 data 字段为空");
                    return;
                }

                String dateStr = dataNode.path("date").asText(null);
                if (dateStr == null || dateStr.trim().isEmpty()) {
                    log.warn("API 返回的日期为空，使用当前日期");
                    dateStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
                }

                LocalDate newsDate;
                try {
                    newsDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    log.error("日期格式解析失败: {}, 使用当前日期", dateStr, e);
                    newsDate = LocalDate.now();
                }

                JsonNode newsArray = dataNode.path("news");
                if (!newsArray.isArray() || newsArray.isEmpty()) {
                    log.warn("API 返回的新闻数组为空");
                    return;
                }

                newsRepository.deleteByDate(newsDate);

                List<News> newsList = new ArrayList<>();
                int index = 0;
                for (JsonNode item : newsArray) {
                    String content = item.asText();
                    if (content == null || content.trim().isEmpty()) {
                        log.warn("跳过空新闻内容，索引: {}", index);
                        index++;
                        continue;
                    }

                    String title = "";
                    String displayContent = content;

                    if (content.matches("^\\d+\\.\\s*.*")) {
                        String[] parts = content.split("\\.\\s*", 2);
                        if (parts.length == 2) {
                            title = parts[1].trim();
                            displayContent = parts[1].trim();
                        } else {
                            title = content.replaceFirst("^\\d+\\.\\s*", "").trim();
                            displayContent = title;
                        }
                    } else {
                        title = content.substring(0, Math.min(50, content.length()));
                    }

                    News news = new News();
                    news.setDate(newsDate);
                    news.setIndexInDay(index);
                    news.setTitle(title);
                    news.setContent(displayContent);
                    news.setCreateTime(LocalDateTime.now());
                    newsList.add(news);
                    index++;
                }

                if (!newsList.isEmpty()) {
                    newsRepository.saveAll(newsList);
                    log.info("成功拉取并存储 {} 条新闻，日期: {}", newsList.size(), newsDate);
                } else {
                    log.warn("没有有效的新闻数据可存储");
                }
            }
        } catch (Exception e) {
            log.error("拉取新闻失败");
        }
    }
}
