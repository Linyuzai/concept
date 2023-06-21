package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceMonitorConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JavaxWebSocketConceptConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new JavaxWebSocketConnectionFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageCodecAdapter messageCodecAdapter() {
        return new JavaxWebSocketMessageCodecAdapter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ConnectionSubscriber.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class LoadBalanceConfiguration extends WebSocketLoadBalanceMonitorConfiguration {

        @Bean
        public ConnectionSubscriber connectionSubscriber(WebSocketLoadBalanceProperties properties) {
            JavaxWebSocketConnectionSubscriber subscriber = new JavaxWebSocketConnectionSubscriber();
            subscriber.setProtocol(properties.getLoadBalance().getProtocol());
            return subscriber;
        }

        @Bean
        public JavaxWebSocketLoadBalanceEndpoint javaxWebSocketLoadBalanceEndpoint(WebSocketLoadBalanceConcept concept) {
            concept.holdInstance();
            return new JavaxWebSocketLoadBalanceEndpoint();
        }
    }
}
