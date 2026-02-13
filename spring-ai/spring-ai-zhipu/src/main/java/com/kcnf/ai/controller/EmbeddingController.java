package com.kcnf.ai.controller;

import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/embedding")
public class EmbeddingController {


    @Autowired
    private ZhiPuAiEmbeddingModel zhiPuAiEmbeddingModel;

    @RequestMapping("/")
    public String index() {
        zhiPuAiEmbeddingModel.embed("hello world");
        return "embedding";
    }
}
