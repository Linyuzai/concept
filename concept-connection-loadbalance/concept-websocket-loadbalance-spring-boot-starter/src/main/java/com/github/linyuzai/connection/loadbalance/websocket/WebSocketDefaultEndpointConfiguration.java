package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.core.extension.UserSelector;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointPathSelector;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointUserMetadataRegister;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * ws 默认端点配置。
 * <p>
 * ws default endpoint configuration.
 */
public class WebSocketDefaultEndpointConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.server.default-endpoint.path-selector",
            name = "enabled", havingValue = "true")
    public DefaultEndpointPathSelector wsDefaultEndpointPathSelector(WebSocketLoadBalanceProperties properties) {
        String prefix = WebSocketLoadBalanceConcept.
                formatPrefix(properties.getServer().getDefaultEndpoint().getPrefix());
        return new DefaultEndpointPathSelector(prefix).addScopes(WebSocketScoped.NAME);
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.server.default-endpoint.user-selector",
            name = "enabled", havingValue = "true")
    public UserSelector wsUserSelector() {
        return new UserSelector().addScopes(WebSocketScoped.NAME);
    }

    @Bean
    @Order(1000)
    @ConditionalOnProperty(prefix = "concept.websocket.server.default-endpoint.user-selector",
            name = "enabled", havingValue = "true")
    public DefaultEndpointUserMetadataRegister wsDefaultEndpointUserMetadataRegister() {
        return new DefaultEndpointUserMetadataRegister();
    }
}
