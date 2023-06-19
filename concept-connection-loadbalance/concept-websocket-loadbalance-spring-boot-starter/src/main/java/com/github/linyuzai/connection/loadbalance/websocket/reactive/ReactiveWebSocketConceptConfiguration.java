package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketScope;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebSocketConceptConfiguration {

    @Bean
    @WebSocketScope
    public ConnectionFactory connectionFactory() {
        return new ReactiveWebSocketConnectionFactory();
    }

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public MessageCodecAdapter messageCodecAdapter() {
        return new ReactiveWebSocketMessageCodecAdapter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ConnectionSubscriber.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public static class ReactiveWebSocketLoadBalanceConfiguration {

        @Bean
        @WebSocketScope
        public ConnectionSubscriber connectionSubscriber(WebSocketLoadBalanceProperties properties) {
            ReactiveWebSocketConnectionSubscriber subscriber = new ReactiveWebSocketConnectionSubscriber();
            subscriber.setProtocol(properties.getLoadBalance().getProtocol());
            return subscriber;
        }

        @Bean
        public ReactiveWebSocketLoadBalanceHandlerMapping reactiveWebSocketLoadBalanceHandlerMapping(WebSocketLoadBalanceConcept concept) {
            return new ReactiveWebSocketLoadBalanceHandlerMapping(concept);
        }
    }
}
