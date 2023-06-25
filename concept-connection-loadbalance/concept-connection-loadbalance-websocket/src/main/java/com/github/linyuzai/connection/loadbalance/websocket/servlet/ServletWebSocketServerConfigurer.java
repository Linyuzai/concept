package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointCustomizer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.config.annotation.ServletWebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 基于 {@link ServletWebSocketConnection} 默认服务的 {@link WebSocketConfigurer}
 */
@RequiredArgsConstructor
public class ServletWebSocketServerConfigurer implements WebSocketConfigurer {

    private final WebSocketLoadBalanceConcept concept;

    private final String prefix;

    private final DefaultEndpointCustomizer customizer;

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        if (registry instanceof ServletWebSocketHandlerRegistry) {
            PrefixUrlPathHelper helper = new PrefixUrlPathHelper(prefix);
            ((ServletWebSocketHandlerRegistry) registry).setUrlPathHelper(helper);
        }
        WebSocketHandlerRegistration registration = registry
                .addHandler(new ServletWebSocketServerHandler(concept), prefix + "**")
                .setAllowedOrigins("*");
        if (customizer != null) {
            customizer.customize(registration);
        }
    }
}
