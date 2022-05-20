package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.core.extension.PathSelector;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointPathSelector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

public class WebSocketDefaultEndpointConfiguration {

    @Bean
    @WebSocketScope
    @ConditionalOnProperty(prefix = "concept.websocket.server.default-endpoint.path-selector",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public PathSelector pathSelector() {
        return new DefaultEndpointPathSelector();
    }
}
