package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketLoadBalanceConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketLoadBalanceConfiguration {

    @Bean
    public ServletWebSocketLoadBalanceConfigurer servletWebSocketLoadBalanceConfigurer(WebSocketLoadBalanceConcept concept) {
        return new ServletWebSocketLoadBalanceConfigurer(concept);
    }
}
