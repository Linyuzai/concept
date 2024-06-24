package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.ServletWebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

/**
 * 基于 {@link ServletWebSocketConnection} 默认服务的 {@link WebSocketConfigurer}。
 * <p>
 * {@link WebSocketConfigurer} for default service based on {@link ServletWebSocketConnection}.
 */
@Getter
@RequiredArgsConstructor
public class ServletWebSocketServerConfigurer implements WebSocketConfigurer {

    private final WebSocketLoadBalanceConcept concept;

    private final String prefix;

    private final List<WebSocketRequestInterceptor> interceptors;

    private final List<DefaultEndpointCustomizer> customizers;

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        if (registry instanceof ServletWebSocketHandlerRegistry) {
            PrefixUrlPathHelper helper = new PrefixUrlPathHelper(prefix);
            ((ServletWebSocketHandlerRegistry) registry).setUrlPathHelper(helper);
        }
        WebSocketHandlerRegistration registration = registry
                .addHandler(new ServletWebSocketServerHandler(concept), prefix + "**")
                .addInterceptors(new ServletWebSocketHandshakeInterceptor())
                .setAllowedOrigins("*");
        for (DefaultEndpointCustomizer customizer : customizers) {
            customizer.customize(registration);
        }
    }

    public class ServletWebSocketHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            WebSocketRequest req = new ServletWebSocketRequest(request);
            WebSocketResponse resp = new ServletWebSocketResponse(response);
            for (WebSocketRequestInterceptor interceptor : interceptors) {
                if (interceptor.intercept(req, resp)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

        }
    }
}
