package com.kcnf.ai.job;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class NewsScheduledTask {

    @Autowired
    private  NewsFetchService fetchService;

    @Scheduled(cron = "${news.fetch.cron}")
    public void fetchNewsJob() {
        log.info("定时任务触发，开始拉取新闻");
        fetchService.fetchAndStore();
    }
}