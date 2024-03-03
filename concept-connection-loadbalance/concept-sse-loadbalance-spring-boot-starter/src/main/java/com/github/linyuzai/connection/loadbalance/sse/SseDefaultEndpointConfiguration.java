package com.github.linyuzai.connection.loadbalance.sse;

import com.github.linyuzai.connection.loadbalance.core.extension.UserSelector;
import com.github.linyuzai.connection.loadbalance.sse.concept.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * SSE 默认端点配置。
 * <p>
 * SSE default endpoint configuration.
 */
public class SseDefaultEndpointConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SseIdGenerator sseIdGenerator() {
        return new DefaultSseIdGenerator();
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.sse.server.default-endpoint.path-selector",
            name = "enabled", havingValue = "true")
    public DefaultEndpointPathSelector sseDefaultEndpointPathSelector() {
        return new DefaultEndpointPathSelector().addScopes(SseScoped.NAME);
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.sse.server.default-endpoint.user-selector",
            name = "enabled", havingValue = "true")
    public UserSelector sseUserSelector() {
        return new UserSelector().addScopes(SseScoped.NAME);
    }

    @Bean
    @Order(1000)
    @ConditionalOnProperty(prefix = "concept.sse.server.default-endpoint.user-selector",
            name = "enabled", havingValue = "true")
    public DefaultEndpointUserMetadataRegister sseDefaultEndpointUserMetadataRegister() {
        return new DefaultEndpointUserMetadataRegister();
    }
}
