package com.kcnf.ai.controller;

import com.kcnf.ai.service.DocumentService;
import com.kcnf.ai.service.RAGService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/rag")
public class RAGController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private RAGService ragService;

    /**
     * 上传文档接口
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            // 将MultipartFile转换为Spring的Resource对象
            documentService.loadDocument(file.getResource());
            return ResponseEntity.ok("文档上传并处理成功！");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("文档处理失败: " + e.getMessage());
        }
    }

    /**
     * 问答接口
     */
    @PostMapping("/ask")
    public Mono<ResponseEntity<String>> askQuestion(@RequestBody QuestionRequest request) {
        return Mono.just(request)
                .map(req -> {
                    String answer = ragService.ask(req.getQuestion());
                    return ResponseEntity.ok(answer);
                });
    }

    // 简单的请求体DTO
    @Getter
    static class QuestionRequest {
        private String question;
    }
}