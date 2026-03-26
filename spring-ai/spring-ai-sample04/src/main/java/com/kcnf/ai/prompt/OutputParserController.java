package com.kcnf.ai.prompt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class OutputParserController {


    @Autowired
    private ChatClient chatClient;

    @GetMapping("/ai/output")
    public ActorsFilms output(@RequestParam(value = "actor", defaultValue = "小猪佩奇") String actor) {
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


    @GetMapping("/ai/map")
    public Map<String, Object> map(@RequestParam(value = "actor", defaultValue = "小猪佩奇") String actor) {
        MapOutputConverter mapOutputConverter = new MapOutputConverter();

        String template = """
        Provide me a List of films for the actor {actor}.
        Return the data as JSON with two fields: 'actor' (the actor name) and 'name movies' (list of movie titles).
        """;

        return chatClient.prompt()
                .user(user -> user.text(template)
                        .param("actor", actor))
                .call()
                .entity(mapOutputConverter);
    }

}
