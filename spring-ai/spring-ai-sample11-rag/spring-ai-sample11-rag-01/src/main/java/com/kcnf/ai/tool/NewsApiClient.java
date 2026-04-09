package com.kcnf.ai.tool;


import com.kcnf.ai.dal.News;
import com.kcnf.ai.dal.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsApiClient {

    @Autowired
    private NewsRepository newsRepository;

    public List<News> searchByKeyword(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return newsRepository.findAll(); // 返回所有，按日期排序
        }

        return newsRepository.searchByKeyword(keyword.trim());
    }
}