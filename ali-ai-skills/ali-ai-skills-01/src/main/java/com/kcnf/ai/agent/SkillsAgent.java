package com.kcnf.ai.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.extension.tools.filesystem.ListFilesTool;
import com.alibaba.cloud.ai.graph.agent.extension.tools.filesystem.ReadFileTool;
import com.alibaba.cloud.ai.graph.agent.extension.tools.filesystem.WriteFileTool;
import com.alibaba.cloud.ai.graph.agent.hook.shelltool.ShellToolAgentHook;
import com.alibaba.cloud.ai.graph.agent.hook.skills.SkillsAgentHook;
import com.alibaba.cloud.ai.graph.agent.tools.ShellTool;
import com.alibaba.cloud.ai.graph.skills.registry.SkillRegistry;
import com.alibaba.cloud.ai.graph.skills.registry.filesystem.FileSystemSkillRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class SkillsAgent {

    private static final String SKILLS_DIR = "ali-ai-skills/ali-ai-skills-01/src/main/resources/skills";



    public ReactAgent buildAgent(ChatModel chatModel) {
        Path skillsPath = Path.of(SKILLS_DIR).toAbsolutePath();
        log.info("Skills directory: {}", skillsPath);

        if (!Files.exists(skillsPath)) {
            log.error("Skills directory not found at: {}", skillsPath);
            throw new IllegalStateException("Skills directory not found");
        }

        log.info("Skills directory exists, listing contents:");
        try {
            Files.list(skillsPath).forEach(p ->
                    log.info("  - {}", p.getFileName())
            );
        } catch (IOException e) {
            log.error("Failed to list directory", e);
        }

        SkillRegistry registry = FileSystemSkillRegistry.builder()
                .projectSkillsDirectory(skillsPath.toString())
                .build();

        SkillsAgentHook hook = SkillsAgentHook.builder()
                .skillRegistry(registry)
                .build();

        List<ToolCallback> tools = new ArrayList<>();
        tools.add(ReadFileTool.createReadFileToolCallback(ReadFileTool.DESCRIPTION));
        tools.add(WriteFileTool.createWriteFileToolCallback(WriteFileTool.DESCRIPTION));
        tools.add(ListFilesTool.createListFilesToolCallback(ListFilesTool.DESCRIPTION));
        tools.add( ShellTool.builder(System.getProperty("user.dir"))
                .build());

        ShellToolAgentHook shellHook = ShellToolAgentHook.builder()
                .shellToolName("shell")
                .build();

        return ReactAgent.builder()
                .name("skill-agent")
                .model(chatModel)
                .hooks(hook, shellHook)
                .tools(tools)
                .enableLogging(true)
                .build();
    }
}



