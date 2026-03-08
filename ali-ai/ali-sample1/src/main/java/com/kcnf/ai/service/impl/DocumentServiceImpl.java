package com.kcnf.ai.service.impl;


import com.kcnf.ai.service.DocumentService;
import org.springframework.stereotype.Service;


@Service
public class DocumentServiceImpl implements DocumentService {

//    private final RedisVectorStore redisVectorStore;
//
//    public DocumentServiceImpl(RedisVectorStore redisVectorStore) {
//        this.redisVectorStore = redisVectorStore;
//    }
//
//    @Override
//    public List<Document> loadText(Resource resource, String fileName) {
//        // 加载读取文档
//        TextReader textReader = new TextReader(resource);
//        textReader.getCustomMetadata().put("fileName", fileName);
//        List<Document> documentList = textReader.get();
//        CustomTextSplitter tokenTextSplitter = new CustomTextSplitter();
//        List<Document> list = tokenTextSplitter.apply(documentList);
//        redisVectorStore.add(list);
//        return documentList;
//    }
//
//    @Override
//    public List<Document> doSearch(String question) {
//        return redisVectorStore.similaritySearch(question);
//    }
}
