package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketServerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.support.WebSocketHandlerMapping;

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(WebSocketHandlerMapping.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketDefaultServerConfiguration {

    @Bean
    public ServletWebSocketServerConfigurer servletWebSocketServerConfigurer(WebSocketLoadBalanceConcept concept) {
        return new ServletWebSocketServerConfigurer(concept);
    }
}
