package org.springaicommunity.skills;

import org.springaicommunity.agent.tools.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder,
			@Value("${agent.skills.dirs:Unknown}") List<Resource> agentSkillsDirs) throws IOException {

		return args -> {

			ChatClient chatClient = chatClientBuilder // @formatter:off
				.defaultSystem("Always use the available skills to assist the user in their requests.")

				// Skills tool
				.defaultToolCallbacks(SkillsTool.builder().addSkillsResources(agentSkillsDirs).build())

				// Built-in tools
				.defaultTools(
					//Bash execution tool
					ShellTools.builder().build(),// built-in shell tools
					// Read, Write and Edit files tool
					FileSystemTools.builder().build(),// built-in file system tools
					// Smart web fetch tool
					SmartWebFetchTool.builder(chatClientBuilder.clone().build()).build(),
					// Brave web search tool
					BraveWebSearchTool.builder("sk-3971f386e73a434f99b425a0b57380ba").resultCount(15).build())
				
				
				.defaultAdvisors(
					// Tool Calling advisor
					ToolCallAdvisor.builder().build(),
					// Custom logging advisor
					MyLoggingAdvisor.builder()
						.showAvailableTools(false)
						.showSystemMessage(false)
						.build())
				.build();
				// @formatter:on

			var answer = chatClient
				.prompt("""
					Explain reinforcement learning in simple terms and use.
					Use required skills.
					Then use the Youtube video https://youtu.be/vXtfdGphr3c?si=xy8U2Al_Um5vE4Jd transcript to support your answer.
					Use absolute paths for the skills and scripts. Do not ask me for more details.
					""")
				.call()
				.content();

			System.out.println("The Answer: " + answer);
		};

	}

}
