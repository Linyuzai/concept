package com.github.linyuzai.connection.loadbalance.sse.servlet;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class DefaultSseEmitterFactory implements SseEmitterFactory {

    @Override
    public SseEmitter create() {
        return new SseEmitter();
    }
}
