package com.github.linyuzai.connection.loadbalance.websocket.web;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
public class SpringWebSocketCustomer implements WebSocketConfigurer {

    private WebSocketLoadBalanceConcept concept;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SpringLoadBalanceWebSocketHandler(concept), "/ws/**");
    }
}
