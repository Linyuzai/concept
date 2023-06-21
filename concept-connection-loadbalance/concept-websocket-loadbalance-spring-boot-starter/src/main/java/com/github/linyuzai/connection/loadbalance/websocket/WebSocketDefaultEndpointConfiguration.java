package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.core.extension.UserSelector;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointPathSelector;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointUserMetadataRegister;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

public class WebSocketDefaultEndpointConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.server.default-endpoint.path-selector",
            name = "enabled", havingValue = "true")
    public DefaultEndpointPathSelector defaultEndpointPathSelector() {
        return new DefaultEndpointPathSelector();
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.server.default-endpoint.user-selector",
            name = "enabled", havingValue = "true")
    public UserSelector userSelector() {
        return new UserSelector();
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.server.default-endpoint.user-selector",
            name = "enabled", havingValue = "true")
    public DefaultEndpointUserMetadataRegister defaultEndpointUserMetadataRegister() {
        return new DefaultEndpointUserMetadataRegister();
    }
}
