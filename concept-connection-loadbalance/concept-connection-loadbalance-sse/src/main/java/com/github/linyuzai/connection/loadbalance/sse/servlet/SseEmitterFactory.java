package com.github.linyuzai.connection.loadbalance.sse.servlet;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterFactory {

    SseEmitter create();
}
