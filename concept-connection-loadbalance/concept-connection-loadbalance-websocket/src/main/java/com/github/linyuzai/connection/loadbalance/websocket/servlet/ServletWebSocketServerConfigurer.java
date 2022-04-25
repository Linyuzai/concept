package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.config.annotation.ServletWebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class ServletWebSocketServerConfigurer implements WebSocketConfigurer {

    private final WebSocketLoadBalanceConcept concept;

    private final WildcardUrlPathHelper helper = new WildcardUrlPathHelper();

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        if (registry instanceof ServletWebSocketHandlerRegistry) {
            ((ServletWebSocketHandlerRegistry) registry).setUrlPathHelper(helper);
        }
        registry.addHandler(new ServletWebSocketServerHandler(concept),
                WebSocketLoadBalanceConcept.SERVER_ENDPOINT_PREFIX + "/**")
                .setAllowedOrigins("*");
    }

    public static class WildcardUrlPathHelper extends UrlPathHelper {

        @Override
        public @NonNull String resolveAndCacheLookupPath(@NonNull HttpServletRequest request) {
            String path = super.resolveAndCacheLookupPath(request);
            if (path.startsWith(WebSocketLoadBalanceConcept.SERVER_ENDPOINT_PREFIX)) {
                return WebSocketLoadBalanceConcept.SERVER_ENDPOINT_PREFIX + "/**";
            }
            return path;
        }
    }
}
