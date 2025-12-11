package com.kcnf.ai.controller;


import com.kcnf.ai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AiController {

    @Autowired
    private ChatService chatService;

    /**
     * @Description: 聊天
     * @Param: [question]
     * @return: reactor.core.publisher.Flux<java.lang.String>
     **/
    @GetMapping("/chat")
    public Flux<String> chat(String question) {
        return chatService.chat(question);
    }
}
