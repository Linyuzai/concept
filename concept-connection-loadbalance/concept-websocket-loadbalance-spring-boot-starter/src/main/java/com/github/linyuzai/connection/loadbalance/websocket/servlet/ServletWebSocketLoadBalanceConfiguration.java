package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointCustomizer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * Servlet WebSocket 负载均衡配置。
 * <p>
 * Servlet WebSocket load balancing configuration.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketLoadBalanceConfiguration {

    @Bean
    public ServletWebSocketConnectionFactory servletWebSocketConnectionFactory() {
        return new ServletWebSocketConnectionFactory();
    }

    @Bean
    public ServletWebSocketMessageCodecAdapter servletWebSocketMessageCodecAdapter() {
        return new ServletWebSocketMessageCodecAdapter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "WEBSOCKET", matchIfMissing = true)
    public static class WebSocketSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.ServletWebSocketConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "WEBSOCKET_SSL")
    public static class WebSocketSSLSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.ServletWebSocketSSLConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @EnableWebSocket
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.server.default-endpoint.enabled",
            havingValue = "true", matchIfMissing = true)
    public static class DefaultEndpointConfiguration extends WebSocketDefaultEndpointConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ServletWebSocketServerConfigurer servletWebSocketServerConfigurer(
                WebSocketLoadBalanceConcept concept,
                WebSocketLoadBalanceProperties properties,
                @Autowired(required = false) DefaultEndpointCustomizer customizer) {
            String prefix = ConnectionLoadBalanceConcept
                    .formatPrefix(properties.getServer().getDefaultEndpoint().getPrefix());
            return new ServletWebSocketServerConfigurer(concept, prefix, customizer);
        }
    }
}
