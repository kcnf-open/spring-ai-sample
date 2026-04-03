package com.kcnf.ai.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcChatMemoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcChatMemoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        initializeSchema();
    }

    private void initializeSchema() {
        String createTableSql = """
            CREATE TABLE IF NOT EXISTS chat_memory (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                conversation_id VARCHAR(255) NOT NULL,
                message_type VARCHAR(50) NOT NULL,
                message_content TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        jdbcTemplate.execute(createTableSql);

        String createIndexSql = """
            CREATE INDEX IF NOT EXISTS idx_conversation_id 
            ON chat_memory(conversation_id)
            """;
        jdbcTemplate.execute(createIndexSql);
    }

    public void save(String conversationId, String messageType, String messageContent) {
        String insertSql = """
            INSERT INTO chat_memory (conversation_id, message_type, message_content)
            VALUES (?, ?, ?)
            """;
        jdbcTemplate.update(insertSql, conversationId, messageType, messageContent);
    }

    public java.util.List<ChatMessage> findByConversationId(String conversationId) {
        String selectSql = """
            SELECT message_type, message_content 
            FROM chat_memory 
            WHERE conversation_id = ? 
            ORDER BY created_at ASC
            """;
        return jdbcTemplate.query(selectSql, (rs, rowNum) -> new ChatMessage(
                rs.getString("message_type"),
                rs.getString("message_content")
        ), conversationId);
    }

    public void deleteByConversationId(String conversationId) {
        String deleteSql = "DELETE FROM chat_memory WHERE conversation_id = ?";
        jdbcTemplate.update(deleteSql, conversationId);
    }

    public static class ChatMessage {
        private final String type;
        private final String content;

        public ChatMessage(String type, String content) {
            this.type = type;
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public String getContent() {
            return content;
        }
    }
}
