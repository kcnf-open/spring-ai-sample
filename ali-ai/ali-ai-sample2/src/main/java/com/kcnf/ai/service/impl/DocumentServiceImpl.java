package com.kcnf.ai.service.impl;


import com.kcnf.ai.service.DocumentService;
import com.kcnf.ai.util.CustomTextSplitter;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DocumentServiceImpl implements DocumentService {

    private final RedisVectorStore redisVectorStore;

    public DocumentServiceImpl(RedisVectorStore redisVectorStore) {
        this.redisVectorStore = redisVectorStore;
    }

    @Override
    public List<Document> loadText(Resource resource, String fileName) {
        // 加载读取文档
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("fileName", fileName);
        List<Document> documentList = textReader.get();
        CustomTextSplitter tokenTextSplitter = new CustomTextSplitter();
        List<Document> list = tokenTextSplitter.apply(documentList);
        redisVectorStore.add(list);
        return documentList;
    }

    @Override
    public List<Document> doSearch(String question) {
        return redisVectorStore.similaritySearch(question);
    }
}
