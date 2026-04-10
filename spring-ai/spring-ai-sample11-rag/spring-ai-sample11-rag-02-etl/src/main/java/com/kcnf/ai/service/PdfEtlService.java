package com.kcnf.ai.service;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PDF ETL 处理服务
 * 负责从 PDF 文档中提取文本、分割、向量化并存储到向量数据库
 */
@Slf4j
@Service
public class PdfEtlService {

    @Autowired
    private  VectorStore vectorStore;

    @Value("classpath:docs/sample.pdf")
    private Resource pdfResource;

    /**
     * 应用启动后自动加载 PDF 并执行 ETL 流水线
     */
    @PostConstruct
    public void loadPdfDocuments() {
        try {
            log.info("Starting PDF ETL pipeline...");
            // 1. Extract - 使用 PagePdfDocumentReader 读取 PDF（按页分割，每页一个 Document）
            DocumentReader pdfReader = new PagePdfDocumentReader(pdfResource);
            List<Document> documents = pdfReader.read();
            log.info("Extracted {} pages from PDF", documents.size());
            // 2. Transform - 使用 TokenTextSplitter 将文档分割成适合 embedding 的 chunks
            TokenTextSplitter splitter = TokenTextSplitter.builder()
                    .withChunkSize(800)          // 每个 chunk 最大 token 数
                    .withMinChunkSizeChars(350)   // 最小 chunk 字符数
                    .build();
            List<Document> splitDocuments = splitter.split(documents);
            log.info("Split into {} chunks", splitDocuments.size());
            // 3. Load - 写入向量存储
            // Domain-specific 风格写法（Spring AI 推荐）
            vectorStore.write(splitDocuments);
            log.info("Successfully loaded {} documents into vector store", splitDocuments.size());
        } catch (Exception e) {
            log.error("Failed to execute PDF ETL pipeline", e);
        }
    }
}