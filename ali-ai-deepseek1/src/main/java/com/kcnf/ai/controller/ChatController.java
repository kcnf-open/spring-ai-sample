package com.kcnf.ai.controller;

import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private DeepSeekChatModel deepSeekChatModel;

    @RequestMapping("/")
    public String index(@RequestParam(value = "name", defaultValue = "World") String name){

        return deepSeekChatModel.call(name);
    }
}
