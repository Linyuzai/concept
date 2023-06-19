package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketScope;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketConceptConfiguration {

    @Bean
    @WebSocketScope
    public ConnectionFactory connectionFactory() {
        return new ServletWebSocketConnectionFactory();
    }

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public MessageCodecAdapter messageCodecAdapter() {
        return new ServletWebSocketMessageCodecAdapter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ConnectionSubscriber.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class ServletWebSocketLoadBalanceConfiguration {

        @Bean
        @WebSocketScope
        public ConnectionSubscriber connectionSubscriber(WebSocketLoadBalanceProperties properties) {
            ServletWebSocketConnectionSubscriber subscriber = new ServletWebSocketConnectionSubscriber();
            subscriber.setProtocol(properties.getLoadBalance().getProtocol());
            return subscriber;
        }

        @Bean
        public ServletWebSocketLoadBalanceConfigurer servletWebSocketLoadBalanceConfigurer(WebSocketLoadBalanceConcept concept) {
            return new ServletWebSocketLoadBalanceConfigurer(concept);
        }
    }
}
