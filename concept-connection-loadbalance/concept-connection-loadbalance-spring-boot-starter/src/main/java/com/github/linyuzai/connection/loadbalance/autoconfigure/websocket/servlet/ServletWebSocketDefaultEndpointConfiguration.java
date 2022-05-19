package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.WebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointConfigurer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketServerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.List;

@EnableWebSocket
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketDefaultEndpointConfiguration extends WebSocketDefaultEndpointConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServletWebSocketServerConfigurer servletWebSocketServerConfigurer(
            WebSocketLoadBalanceConcept concept,
            List<DefaultEndpointConfigurer> configurers) {
        return new ServletWebSocketServerConfigurer(concept, configurers);
    }
}
