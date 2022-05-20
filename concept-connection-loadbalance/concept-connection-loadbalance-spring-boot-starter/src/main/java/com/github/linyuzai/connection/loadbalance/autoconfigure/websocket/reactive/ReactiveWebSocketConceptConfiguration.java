package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.WebSocketScope;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketConnectionFactory;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketMessageCodecAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebSocketConceptConfiguration {

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public ConnectionFactory connectionFactory() {
        return new ReactiveWebSocketConnectionFactory();
    }

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public ConnectionSubscriber connectionSubscriber(WebSocketLoadBalanceProperties properties) {
        return new ReactiveWebSocketConnectionSubscriber(properties.getLoadBalance().getProtocol());
    }

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public MessageCodecAdapter messageCodecAdapter() {
        return new ReactiveWebSocketMessageCodecAdapter();
    }
}
