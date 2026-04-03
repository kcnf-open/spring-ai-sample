package com.kcnf.ai.controller;


import com.kcnf.ai.controller.model.SimilarityRequest;
import com.kcnf.ai.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/embedding")
public class EmbeddingController {

    @Autowired
    private EmbeddingService embeddingService;

    /**
     * GET 将文本转为向量
     */
    @GetMapping
    public Map<String, Object> embed(@RequestParam String text) {
        float[] vector = embeddingService.embed(text);
        return Map.of(
                "text", text,
                "dimension", vector.length,
                "vector", vector
        );
    }

    /**
     * POST 批量转换文本为向量
     */
    @PostMapping("/batch")
    public Map<String, Object> embedBatch(@RequestBody List<String> texts) {
        List<float[]> vectors = embeddingService.embedBatch(texts);
        return Map.of(
                "count", texts.size(),
                "dimension", vectors.isEmpty() ? 0 : vectors.get(0).length,
                "vectors", vectors
        );
    }

    /**
     * POST 计算两段文本的相似度
     * Body: {"text1": "Java是一种编程语言", "text2": "Python是一种编程语言"}
     */
    @PostMapping("/similarity")
    public Map<String, Object> similarity(@RequestBody SimilarityRequest request) {
        double score = embeddingService.similarity(request.getText1(), request.getText2());
        return Map.of(
                "text1", request.getText1(),
                "text2", request.getText2(),
                "similarity", score
        );
    }
}