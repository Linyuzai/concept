package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketConnectionFactory;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketLoadBalanceEndpoint;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketSubscriberEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(ServerEndpointExporter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JavaxWebSocketConceptConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConnectionFactory connectionFactory() {
        return new JavaxWebSocketConnectionFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionSubscriber connectionSubscriber() {
        return new JavaxWebSocketConnectionSubscriber();
    }
}
