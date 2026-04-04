package com.kcnf.ai.controller;

import com.kcnf.ai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // 方便前端调试
public class ChatController {

    @Autowired
    private ChatService chatService;


    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody ChatRequest request) {
        String reply = chatService.chat(request.conversationId(), request.message());
        return Map.of("reply", reply);
    }

    public record ChatRequest(String conversationId, String message) {}
}
