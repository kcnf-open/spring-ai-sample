package com.kcnf.ai.service;

import reactor.core.publisher.Flux;

public interface ChatService {

    Flux<String> chat(String question);
}
