package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketLoadBalanceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServletWebSocketLoadBalanceConfigurer servletWebSocketLoadBalanceConfigurer(WebSocketLoadBalanceConcept concept) {
        return new ServletWebSocketLoadBalanceConfigurer(concept);
    }
}
