package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketLoadBalanceHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebSocketLoadBalanceConfiguration {

    @Bean
    public ReactiveWebSocketLoadBalanceHandlerMapping reactiveWebSocketLoadBalanceHandlerMapping(WebSocketLoadBalanceConcept concept) {
        return new ReactiveWebSocketLoadBalanceHandlerMapping(concept);
    }
}
