package com.kcnf.ai.controller;

import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    @Qualifier("deepSeekChatModel")
    private DeepSeekChatModel deepSeekChatModel;

    @GetMapping("/t")
    public String index(@RequestParam(value = "name", defaultValue = "World") String name){
        return deepSeekChatModel.call(name);
    }
}
