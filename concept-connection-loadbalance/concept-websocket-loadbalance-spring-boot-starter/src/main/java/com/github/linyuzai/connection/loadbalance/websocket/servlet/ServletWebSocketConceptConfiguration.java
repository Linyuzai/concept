package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketScope;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketConceptConfiguration {

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public ConnectionFactory connectionFactory() {
        return new ServletWebSocketConnectionFactory();
    }

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public ConnectionSubscriber connectionSubscriber(WebSocketLoadBalanceProperties properties) {
        return new ServletWebSocketConnectionSubscriber(properties.getLoadBalance().getProtocol());
    }

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public MessageCodecAdapter messageCodecAdapter() {
        return new ServletWebSocketMessageCodecAdapter();
    }
}
