package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.WebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceMonitorConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointCustomizer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketLoadBalanceConfiguration {

    @Bean
    public ServletWebSocketConnectionFactory servletWebSocketConnectionFactory() {
        return new ServletWebSocketConnectionFactory();
    }

    @Bean
    public ServletWebSocketMessageCodecAdapterFactory servletWebSocketMessageCodecAdapterFactory() {
        return new ServletWebSocketMessageCodecAdapterFactory();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber", havingValue = "WEBSOCKET", matchIfMissing = true)
    public static class WebSocketSubscriberConfiguration extends WebSocketLoadBalanceMonitorConfiguration {

        @Bean
        public ServletWebSocketConnectionSubscriberFactory servletWebSocketConnectionSubscriberFactory(WebSocketLoadBalanceProperties properties) {
            ServletWebSocketConnectionSubscriberFactory factory = new ServletWebSocketConnectionSubscriberFactory();
            factory.setProtocol(properties.getLoadBalance().getProtocol());
            return factory;
        }

        @Bean
        public ServletWebSocketLoadBalanceConfigurer servletWebSocketLoadBalanceConfigurer(WebSocketLoadBalanceConcept concept) {
            return new ServletWebSocketLoadBalanceConfigurer(concept);
        }
    }

    @EnableWebSocket
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.server.default-endpoint.enabled", havingValue = "true", matchIfMissing = true)
    public static class DefaultEndpointConfiguration extends WebSocketDefaultEndpointConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ServletWebSocketServerConfigurer servletWebSocketServerConfigurer(
                WebSocketLoadBalanceConcept concept,
                @Autowired(required = false) DefaultEndpointCustomizer customizer) {
            return new ServletWebSocketServerConfigurer(concept, customizer);
        }
    }
}
