package com.kcnf.ai.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class AiConfig {

    @Value("${classpath:data/vector-store.json}")
    private String vectorStorePath;

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        File storeFile = new File(vectorStorePath);
        if (!storeFile.getParentFile().exists()) {
            storeFile.getParentFile().mkdirs();
        }
        SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();
        if (storeFile.exists() && storeFile.length() > 0) {
            try {
                store.load(storeFile);
                log.info("向量存储加载成功: {}", storeFile.getAbsolutePath());
            } catch (Exception e) {
                log.warn("向量存储加载失败，将使用空存储", e);
            }
        }
        CountDownLatch saveLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                log.info("开始保存向量存储到: {}", storeFile.getAbsolutePath());
                long startTime = System.currentTimeMillis();
                store.save(storeFile);
                long duration = System.currentTimeMillis() - startTime;
                log.info("向量存储已保存成功，耗时: {}ms, 文件大小: {} bytes",
                        duration, storeFile.length());
            } catch (Exception e) {
                log.error("保存向量存储失败", e);
            } finally {
                saveLatch.countDown();
            }
        }, "vector-store-save-hook"));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (!saveLatch.await(10, TimeUnit.SECONDS)) {
                    log.warn("向量存储保存超时");
                }
            } catch (InterruptedException e) {
                log.error("等待向量存储保存被中断", e);
                Thread.currentThread().interrupt();
            }
        }, "vector-store-save-waiter"));
        return store;
    }

}