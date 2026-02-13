package com.kcnf.ai.controller;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/embedding")
public class EmbeddingController {


    @Autowired
    private EmbeddingModel zhiPuAiEmbeddingModel;

    @RequestMapping("/")
    public String index() {
        zhiPuAiEmbeddingModel.embed("hello world");
        return "embedding";
    }
}
