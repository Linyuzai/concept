package com.github.linyuzai.concept.sample.connection;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.sender.CompletableFutureMessageSenderFactory;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleConfig {

    @Bean
    public CompletableFutureMessageSenderFactory completableFutureMessageSenderFactory() {
        return new CompletableFutureMessageSenderFactory().addScopes(WebSocketScoped.NAME);
    }
}
