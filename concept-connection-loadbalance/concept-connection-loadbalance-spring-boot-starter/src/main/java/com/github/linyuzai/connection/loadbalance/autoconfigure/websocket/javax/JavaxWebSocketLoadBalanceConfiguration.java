package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketLoadBalanceEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JavaxWebSocketLoadBalanceConfiguration {

    @Bean
    public JavaxWebSocketLoadBalanceEndpoint javaxWebSocketLoadBalanceEndpoint(WebSocketLoadBalanceConcept concept) {
        concept.holdInstance();
        return new JavaxWebSocketLoadBalanceEndpoint();
    }
}
