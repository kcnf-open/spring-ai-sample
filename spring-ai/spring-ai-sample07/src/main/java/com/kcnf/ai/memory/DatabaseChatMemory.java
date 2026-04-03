package com.kcnf.ai.memory;

import com.kcnf.ai.repository.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DatabaseChatMemory implements ChatMemory {

    private final JdbcChatMemoryRepository repository;

    public DatabaseChatMemory(JdbcChatMemoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        for (Message message : messages) {
            String messageType;
            if (message instanceof UserMessage) {
                messageType = "USER";
            } else if (message instanceof AssistantMessage) {
                messageType = "ASSISTANT";
            } else {
                continue;
            }
            repository.save(conversationId, messageType, message.getText());
        }
    }

    @Override
    public List<Message> get(String conversationId) {
        List<JdbcChatMemoryRepository.ChatMessage> dbMessages =
                repository.findByConversationId(conversationId);

        List<Message> messages = new ArrayList<>();
        for (JdbcChatMemoryRepository.ChatMessage dbMessage : dbMessages) {
            if ("USER".equals(dbMessage.getType())) {
                messages.add(new UserMessage(dbMessage.getContent()));
            } else if ("ASSISTANT".equals(dbMessage.getType())) {
                messages.add(new AssistantMessage(dbMessage.getContent()));
            }
        }
        return messages;
    }

    @Override
    public void clear(String conversationId) {
        repository.deleteByConversationId(conversationId);
    }
}
