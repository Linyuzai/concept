package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.WebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointCustomizer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketDefaultEndpointConfiguration extends WebSocketDefaultEndpointConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServletWebSocketServerConfigurer servletWebSocketServerConfigurer(
            WebSocketLoadBalanceConcept concept,
            @Autowired(required = false) DefaultEndpointCustomizer customizer) {
        return new ServletWebSocketServerConfigurer(concept, customizer);
    }
}
