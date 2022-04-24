package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketServerHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebSocketDefaultServerConfiguration {

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public ReactiveWebSocketServerHandlerMapping reactiveWebSocketServerHandlerMapping(WebSocketLoadBalanceConcept concept) {
        return new ReactiveWebSocketServerHandlerMapping(concept);
    }
}
