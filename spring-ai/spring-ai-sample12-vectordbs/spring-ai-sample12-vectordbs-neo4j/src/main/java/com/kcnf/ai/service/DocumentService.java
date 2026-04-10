package com.kcnf.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DocumentService {

    @Autowired
    private VectorStore vectorStore;

    /**
     * 处理上传的文本文件，将其分块并存入向量数据库。
     * @param resource 上传的文本文件资源
     */
    public void loadDocument(Resource resource) {
        log.info("开始处理文档上传: {}", resource.getFilename());

        var textReader = new TextReader(resource);
        List<Document> documents = textReader.get();
        log.debug("原始文档读取完成，共 {} 个 Document 对象", documents.size());

        var tokenTextSplitter = new TokenTextSplitter();
        List<Document> splitDocuments = tokenTextSplitter.apply(documents);
        log.info("文档分块完成，共生成 {} 个文本块", splitDocuments.size());

        vectorStore.add(splitDocuments);
        log.info("所有文本块已存入向量库");
    }
}