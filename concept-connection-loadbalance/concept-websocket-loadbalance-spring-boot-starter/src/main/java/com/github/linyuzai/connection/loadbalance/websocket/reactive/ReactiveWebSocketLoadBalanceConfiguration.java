package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.WebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointCustomizer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebSocketLoadBalanceConfiguration {

    @Bean
    public ReactiveWebSocketConnectionFactory reactiveWebSocketConnectionFactory() {
        return new ReactiveWebSocketConnectionFactory();
    }

    @Bean
    public ReactiveWebSocketMessageCodecAdapter reactiveWebSocketMessageCodecAdapter() {
        return new ReactiveWebSocketMessageCodecAdapter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "WEBSOCKET", matchIfMissing = true)
    public static class WebSocketSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.ReactiveWebSocketConfiguration
            implements WebSocketSubscriberConfiguration.MasterIndexProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave1",
            havingValue = "WEBSOCKET")
    public static class WebSocketSubscriberSlave1Configuration
            extends WebSocketSubscriberConfiguration.ReactiveWebSocketConfiguration
            implements WebSocketSubscriberConfiguration.Slave1IndexProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "WEBSOCKET_SSL")
    public static class WebSocketSSLSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.ReactiveWebSocketSSLConfiguration
            implements WebSocketSubscriberConfiguration.MasterIndexProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave1",
            havingValue = "WEBSOCKET_SSL")
    public static class WebSocketSSLSubscriberSlave1Configuration
            extends WebSocketSubscriberConfiguration.ReactiveWebSocketSSLConfiguration
            implements WebSocketSubscriberConfiguration.Slave1IndexProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.server.default-endpoint.enabled",
            havingValue = "true", matchIfMissing = true)
    public static class DefaultEndpointConfiguration extends WebSocketDefaultEndpointConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public WebSocketHandlerAdapter webSocketHandlerAdapter() {
            return new WebSocketHandlerAdapter();
        }

        @Bean
        @ConditionalOnMissingBean
        public ReactiveWebSocketServerHandlerMapping reactiveWebSocketServerHandlerMapping(
                WebSocketLoadBalanceConcept concept,
                WebSocketLoadBalanceProperties properties,
                @Autowired(required = false) DefaultEndpointCustomizer customizer) {
            String prefix = WebSocketLoadBalanceConcept
                    .formatPrefix(properties.getServer().getDefaultEndpoint().getPrefix());
            return new ReactiveWebSocketServerHandlerMapping(concept, prefix, customizer);
        }
    }
}
