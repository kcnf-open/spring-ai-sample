package com.kcnf.ai.service;


import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {

    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 将单个文本转换为向量
     */
    public float[] embed(String text) {
        EmbeddingResponse response = embeddingModel.embedForResponse(List.of(text));
        if (!response.getResults().isEmpty()) {
            return response.getResults().get(0).getOutput();
        }
        return new float[0];
    }

    /**
     * 批量将多个文本转换为向量
     */
    public List<float[]> embedBatch(List<String> texts) {
        EmbeddingRequest request = new EmbeddingRequest(texts, null);
        EmbeddingResponse response = embeddingModel.call(request);
        return response.getResults().stream()
                .map(Embedding::getOutput)
                .toList();
    }

    /**
     * 计算两段文本的语义相似度（余弦相似度）
     */
    public double similarity(String text1, String text2) {
        float[] vec1 = embed(text1);
        float[] vec2 = embed(text2);

        if (vec1.length == 0 || vec2.length == 0 || vec1.length != vec2.length) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }
        if (norm1 == 0 || norm2 == 0) return 0.0;
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}