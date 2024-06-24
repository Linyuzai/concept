package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.concept.DefaultEndpointCustomizer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.List;

/**
 * Reactive WebSocket 负载均衡配置。
 * <p>
 * Reactive WebSocket load balancing configuration.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebSocketLoadBalanceConfiguration extends WebSocketLoadBalanceConfiguration {

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
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "WEBSOCKET_SSL")
    public static class WebSocketSSLSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.ReactiveWebSocketSSLConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.server.default-endpoint.enabled",
            havingValue = "true", matchIfMissing = true)
    public static class DefaultEndpointConfiguration extends WebSocketDefaultEndpointConfiguration {

        @Bean
        public WebSocketHandlerAdapter webSocketHandlerAdapter(List<WebSocketRequestInterceptor> interceptors) {
            return new WebSocketHandlerAdapter(new ReactiveWebSocketHandshakeService(interceptors));
        }

        @Bean
        public ReactiveWebSocketServerHandlerMapping reactiveWebSocketServerHandlerMapping(
                WebSocketLoadBalanceConcept concept,
                WebSocketLoadBalanceProperties properties,
                List<DefaultEndpointCustomizer> customizers) {
            String prefix = ConnectionLoadBalanceConcept
                    .formatPrefix(properties.getServer().getDefaultEndpoint().getPrefix());
            return new ReactiveWebSocketServerHandlerMapping(concept, prefix, customizers);
        }
    }
}
