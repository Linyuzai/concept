package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax;

import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.WebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketServerEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JavaxWebSocketDefaultEndpointConfiguration extends WebSocketDefaultEndpointConfiguration {

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
