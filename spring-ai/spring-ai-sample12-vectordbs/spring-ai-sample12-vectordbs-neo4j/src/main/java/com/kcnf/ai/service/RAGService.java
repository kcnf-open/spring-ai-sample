package com.kcnf.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RAGService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;


    /**
     * 基于RAG的问答方法
     * @param userQuery 用户的问题
     * @return AI生成的回答
     */
    public String ask(String userQuery) {
        log.info("收到用户提问: {}", userQuery);
        // 构建一个QuestionAnswerAdvisor，它将自动从VectorStore中检索相关上下文
        var questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                // 可以配置检索参数，例如设置相似度阈值和返回的文档块数量
                // .searchRequest(SearchRequest.builder().similarityThreshold(0.8d).topK(6).build())
                .build();
        log.info("AI 回答生成完成");
        // 使用ChatClient，并添加RAG顾问
        return this.chatClient.prompt()
                .user(userQuery)
                .advisors(questionAnswerAdvisor)
                .call()
                .content();
    }
}