package com.kcnf.ai.prompt;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PromptController {


    @Autowired
    private  ChatClient chatClient;

    @Value("classpath:/prompts/system-message.st")
    private Resource jokeResource;


    @GetMapping("/ai/simple")
    public Map<String, String> simple(
            @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        try {
            String response = chatClient.prompt(message).call().content();
            return Map.of("generation", response);
        } catch (Exception e) {
            return Map.of("error", "Failed to generate response: " + e.getMessage());
        }
    }


    @GetMapping("/ai/prompt")
    public Map<String, String> prompt(@RequestParam(value = "adjective", defaultValue = "funny") String adjective,
                                      @RequestParam(value = "topic", defaultValue = "cows") String topic) {
        try {
            PromptTemplate promptTemplate = new PromptTemplate(jokeResource);
            Prompt prompt = promptTemplate.create(Map.of(
                    "adjective", adjective,
                    "topic", topic,
                    "name", "AI Assistant",
                    "voice", "friendly and professional"
            ));
            String response = chatClient.prompt(prompt).call().content();
            return Map.of("result", response);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Failed to generate completion: " + e.getMessage());
        }
    }
}
