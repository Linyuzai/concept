package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveLoadBalanceWebSocketHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebSocketConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(WebSocketHandlerAdapter.class)
    public ReactiveLoadBalanceWebSocketHandlerMapping reactiveLoadBalanceWebSocketHandlerMapping(WebSocketLoadBalanceConcept concept) {
        return new ReactiveLoadBalanceWebSocketHandlerMapping(concept);
    }
}
