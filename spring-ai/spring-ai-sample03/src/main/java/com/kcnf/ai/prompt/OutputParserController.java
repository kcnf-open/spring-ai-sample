package com.kcnf.ai.prompt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OutputParserController {


    @Autowired
    private ChatClient chatClient;

    @GetMapping("/ai/output")
    public ActorsFilms generate(@RequestParam(value = "actor", defaultValue = "小猪佩奇") String actor) {
        var outputConverter = new BeanOutputConverter<>(ActorsFilms.class);

        String userMessage = """
                Generate the filmography for the actor {actor}.
                Provide the output in JSON format that matches the following structure:
                Actor name and list of movies.
                """;

        return chatClient.prompt()
                .user(user -> user.text(userMessage)
                        .param("actor", actor))
                .call()
                .entity(outputConverter);
    }

}
