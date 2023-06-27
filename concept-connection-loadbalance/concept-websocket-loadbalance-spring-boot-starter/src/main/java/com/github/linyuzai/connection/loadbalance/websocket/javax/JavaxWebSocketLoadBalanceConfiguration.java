package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.websocket.WebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.WebSocketSubscriberConfiguration;
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
    public JavaxWebSocketMessageCodecAdapter javaxWebSocketMessageCodecAdapter() {
        return new JavaxWebSocketMessageCodecAdapter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "WEBSOCKET", matchIfMissing = true)
    public static class WebSocketSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.JavaxWebSocketConfiguration
            implements WebSocketSubscriberConfiguration.MasterIndexProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave1",
            havingValue = "WEBSOCKET")
    public static class WebSocketSubscriberSlave1Configuration
            extends WebSocketSubscriberConfiguration.JavaxWebSocketConfiguration
            implements WebSocketSubscriberConfiguration.Slave1IndexProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "WEBSOCKET_SSL")
    public static class WebSocketSSLSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.JavaxWebSocketSSLConfiguration
            implements WebSocketSubscriberConfiguration.MasterIndexProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave1",
            havingValue = "WEBSOCKET_SSL")
    public static class WebSocketSSLSubscriberSlave1Configuration
            extends WebSocketSubscriberConfiguration.JavaxWebSocketSSLConfiguration
            implements WebSocketSubscriberConfiguration.Slave1IndexProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.server.default-endpoint.enabled",
            havingValue = "true", matchIfMissing = true)
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
