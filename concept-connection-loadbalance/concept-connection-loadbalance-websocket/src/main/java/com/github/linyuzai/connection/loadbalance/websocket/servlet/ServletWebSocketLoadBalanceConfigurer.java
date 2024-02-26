package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 基于 {@link ServletWebSocketConnection} 的服务间负载均衡的 {@link WebSocketConfigurer}。
 * <p>
 * {@link WebSocketConfigurer} for service load balancing based on {@link ServletWebSocketConnection}.
 */
@Getter
@RequiredArgsConstructor
public class ServletWebSocketLoadBalanceConfigurer implements WebSocketConfigurer {

    private final WebSocketLoadBalanceConcept concept;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ServletWebSocketLoadBalanceHandler(concept),
                        WebSocketLoadBalanceConcept.SUBSCRIBER_ENDPOINT)
                .setAllowedOrigins("*");
    }
}
