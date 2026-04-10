package com.kcnf.ai.controller;

import com.kcnf.ai.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    @Autowired
    private RagService ragService;


    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody QuestionRequest request) {
        String answer = ragService.ask(request.question());
        return Map.of("question", request.question(), "answer", answer);
    }

    @PostMapping("/search")
    public List<String> search(@RequestBody QuestionRequest request) {
        return ragService.searchRelevantDocuments(request.question, 3);
    }

    public record QuestionRequest(String question) {}
}