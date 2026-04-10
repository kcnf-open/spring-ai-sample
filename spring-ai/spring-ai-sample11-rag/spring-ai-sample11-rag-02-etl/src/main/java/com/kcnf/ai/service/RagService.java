package com.kcnf.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RAG 问答服务
 * 基于已加载的向量存储执行检索增强生成
 */
@Slf4j
@Service
public class RagService {


    @Autowired
    private  ChatClient chatClient;
    @Autowired
    private  VectorStore vectorStore;


    /**
     * 基于 RAG 回答用户问题
     * @param question 用户问题
     * @return AI 生成的回答
     */
    public String ask(String question) {
        log.info("RAG query: {}", question);
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

    /**
     * 仅检索相关文档，不调用 LLM 生成回答（用于调试）
     */
    public List<String> searchRelevantDocuments(String query, int topK) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build();

        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        if (CollectionUtils.isEmpty(documents)) {
            return Collections.emptyList();
        }
        return documents.stream()
                .map(Document::getText)
                .collect(Collectors.toList());
    }
}