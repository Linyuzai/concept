package com.github.linyuzai.connection.loadbalance.websocket.reactive;

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
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebSocketLoadBalanceConfiguration {

    @Bean
    public ReactiveWebSocketConnectionFactory reactiveWebSocketConnectionFactory() {
        return new ReactiveWebSocketConnectionFactory();
    }

    @Bean
    public ReactiveWebSocketMessageCodecAdapterFactory reactiveWebSocketMessageCodecAdapterFactory() {
        return new ReactiveWebSocketMessageCodecAdapterFactory();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber", havingValue = "WEBSOCKET", matchIfMissing = true)
    public static class WebSocketSubscriberConfiguration extends WebSocketLoadBalanceMonitorConfiguration {

        @Bean
        public ReactiveWebSocketConnectionSubscriberFactory reactiveWebSocketConnectionSubscriberFactory(WebSocketLoadBalanceProperties properties) {
            ReactiveWebSocketConnectionSubscriberFactory factory = new ReactiveWebSocketConnectionSubscriberFactory();
            factory.setProtocol(properties.getLoadBalance().getProtocol());
            return factory;
        }

        @Bean
        public ReactiveWebSocketLoadBalanceHandlerMapping reactiveWebSocketLoadBalanceHandlerMapping(WebSocketLoadBalanceConcept concept) {
            return new ReactiveWebSocketLoadBalanceHandlerMapping(concept);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.server.default-endpoint.enabled", havingValue = "true", matchIfMissing = true)
    public static class DefaultEndpointConfiguration extends WebSocketDefaultEndpointConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public WebSocketHandlerAdapter webSocketHandlerAdapter() {
            return new WebSocketHandlerAdapter();
        }

        @Bean
        @ConditionalOnMissingBean
        public ReactiveWebSocketServerHandlerMapping reactiveWebSocketServerHandlerMapping(
                WebSocketLoadBalanceConcept concept,
                @Autowired(required = false) DefaultEndpointCustomizer customizer) {
            return new ReactiveWebSocketServerHandlerMapping(concept, customizer);
        }
    }
}
