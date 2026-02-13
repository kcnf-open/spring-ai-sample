package com.kcnf.ai.controller;


import com.kcnf.ai.service.ChatService;
import com.kcnf.ai.service.DocumentService;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class RagController {


    @Resource
    private DocumentService documentService;

    @Resource
    private ChatService chatService;

    @PostMapping("/uploadRagDoc")
    public List<Document> uploadRagDoc(@RequestParam("file") MultipartFile file ){
        List<Document> documentList =  documentService.loadText(file.getResource(), file.getOriginalFilename());
        return documentList;
    }
}
