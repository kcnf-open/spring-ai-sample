CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT,
    type VARCHAR(255),
    timestamp TIMESTAMP,
    conversation_id VARCHAR(255)
);


-- 创建新闻表
CREATE TABLE IF NOT EXISTS news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    date DATE NOT NULL COMMENT '新闻日期',
    index_in_day INT NOT NULL COMMENT '当天序号（第几条新闻）',
    title VARCHAR(500) COMMENT '新闻标题',
    content TEXT NOT NULL COMMENT '新闻内容',
    create_time TIMESTAMP NOT NULL COMMENT '记录创建时间',

    -- 唯一约束：同一天内序号不能重复
    CONSTRAINT uk_news_date_index UNIQUE (date, index_in_day)
);