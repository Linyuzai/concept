package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.websocket.WebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceMonitorConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketLoadBalanceProperties;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JavaxWebSocketLoadBalanceConfiguration {

    @Bean
    public JavaxWebSocketConnectionFactory javaxWebSocketConnectionFactory() {
        return new JavaxWebSocketConnectionFactory();
    }

    @Bean
    public JavaxWebSocketMessageCodecAdapterFactory javaxWebSocketMessageCodecAdapterFactory() {
        return new JavaxWebSocketMessageCodecAdapterFactory();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.protocol", havingValue = "WEBSOCKET", matchIfMissing = true)
    public static class WebSocketProtocolConfiguration extends WebSocketLoadBalanceMonitorConfiguration {

        @Bean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory() {
            JavaxWebSocketConnectionSubscriberFactory factory = new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            return factory;
        }

        @Bean
        public JavaxWebSocketLoadBalanceEndpoint javaxWebSocketLoadBalanceEndpoint(WebSocketLoadBalanceConcept concept) {
            concept.holdInstance();
            return new JavaxWebSocketLoadBalanceEndpoint();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.protocol", havingValue = "WEBSOCKET_SSL")
    public static class WebSocketSSLProtocolConfiguration extends WebSocketLoadBalanceMonitorConfiguration {

        @Bean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory() {
            JavaxWebSocketConnectionSubscriberFactory factory = new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            return factory;
        }

        @Bean
        public JavaxWebSocketLoadBalanceEndpoint javaxWebSocketLoadBalanceEndpoint(WebSocketLoadBalanceConcept concept) {
            concept.holdInstance();
            return new JavaxWebSocketLoadBalanceEndpoint();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.server.default-endpoint.enabled", havingValue = "true", matchIfMissing = true)
    public static class DefaultEndpointConfiguration extends WebSocketDefaultEndpointConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ServerEndpointExporter serverEndpointExporter() {
            return new ServerEndpointExporter();
        }

        @Bean
        @ConditionalOnMissingBean
        public JavaxWebSocketServerEndpoint javaxWebSocketServerEndpoint() {
            return new JavaxWebSocketServerEndpoint();
        }
    }
}
