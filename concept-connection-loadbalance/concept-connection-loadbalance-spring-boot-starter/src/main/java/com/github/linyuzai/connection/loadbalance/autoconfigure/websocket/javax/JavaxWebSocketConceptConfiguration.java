package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax;

import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.WebSocketScope;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketConnectionFactory;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketMessageCodecAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JavaxWebSocketConceptConfiguration {

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public ConnectionFactory connectionFactory() {
        return new JavaxWebSocketConnectionFactory();
    }

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public ConnectionSubscriber connectionSubscriber(WebSocketLoadBalanceProperties properties) {
        return new JavaxWebSocketConnectionSubscriber(properties.getLoadBalance().getProtocol());
    }

    @Bean
    @WebSocketScope
    @ConditionalOnMissingBean
    public MessageCodecAdapter messageCodecAdapter() {
        return new JavaxWebSocketMessageCodecAdapter();
    }
}
