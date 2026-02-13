package com.kcnf.ai.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class RagService {

    @Autowired
    private EmbeddingModel embeddingModel;


    public RagService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        ClassPathResource resource = new ClassPathResource("Embedding.txt");

    }
}
