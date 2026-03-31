package com.kcnf.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MultimodalController {

    @Autowired
    private ChatClient chatClient;

    @GetMapping("/describe-image")
    public String describeImage() {
        var image = new ClassPathResource("static/bb.jpg");

        Media media = new Media(MimeTypeUtils.IMAGE_JPEG, image);

        var userMessage = UserMessage.builder()
                .text("详细描述这张图片中的产品")
                .media(media)
                .build();

        return chatClient.prompt(new Prompt(List.of(userMessage))).call().content();
    }
}
