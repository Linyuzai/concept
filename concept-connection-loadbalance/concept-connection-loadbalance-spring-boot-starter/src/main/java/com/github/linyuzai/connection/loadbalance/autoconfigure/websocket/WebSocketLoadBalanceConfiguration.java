package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatAutoSender;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.monitor.ConnectionSubscribeMonitor;
import com.github.linyuzai.connection.loadbalance.core.subscribe.monitor.ScheduledExecutorConnectionSubscribeMonitor;
import com.github.linyuzai.connection.loadbalance.core.extension.ScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebSocketLoadBalanceConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.load-balance.subscriber.monitor",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ScheduledExecutorConnectionSubscribeMonitor scheduledExecutorConnectionSubscribeMonitor(
            ScheduledExecutorServiceFactory factory,
            WebSocketLoadBalanceProperties properties) {
        return new ScheduledExecutorConnectionSubscribeMonitor(
                factory.create(ConnectionSubscribeMonitor.class),
                properties.getSubscriber().getMonitor().getPeriod());
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.load-balance.subscriber.heartbeat",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatAutoSender loadBalanceConnectionHeartbeatAutoSender(
            ScheduledExecutorServiceFactory factory,
            WebSocketLoadBalanceProperties properties) {
        WebSocketLoadBalanceProperties.HeartbeatProperties heartbeat = properties.getSubscriber().getHeartbeat();
        return new ConnectionHeartbeatAutoSender(
                Arrays.asList(Connection.Type.SUBSCRIBER, Connection.Type.OBSERVABLE),
                heartbeat.getTimeout(), heartbeat.getPeriod(),
                factory.create(ConnectionHeartbeatAutoSender.class));
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.load-balance.server.heartbeat",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatAutoSender clientConnectionHeartbeatSender(
            ScheduledExecutorServiceFactory factory,
            WebSocketLoadBalanceProperties properties) {
        WebSocketLoadBalanceProperties.HeartbeatProperties heartbeat = properties.getServer().getHeartbeat();
        return new ConnectionHeartbeatAutoSender(Connection.Type.CLIENT,
                heartbeat.getTimeout(), heartbeat.getPeriod(),
                factory.create(ConnectionHeartbeatAutoSender.class));
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public WebSocketLoadBalanceConcept webSocketLoadBalanceConcept(
            ConnectionRepository repository,
            ConnectionServerProvider provider,
            ConnectionSubscriber subscriber,
            List<ConnectionFactory> connectionFactories,
            List<ConnectionSelector> connectionSelectors,
            List<MessageFactory> messageFactories,
            MessageCodecAdapter messageCodecAdapter,
            ConnectionEventPublisher eventPublisher,
            List<ConnectionEventListener> eventListeners) {
        return new WebSocketLoadBalanceConcept.Builder()
                .connectionRepository(repository)
                .connectionServerProvider(provider)
                .connectionSubscriber(subscriber)
                .addConnectionFactories(connectionFactories)
                .addConnectionSelectors(connectionSelectors)
                .addMessageFactories(messageFactories)
                .messageCodecAdapter(messageCodecAdapter)
                .eventPublisher(eventPublisher)
                .addEventListeners(eventListeners)
                .build();
    }
}
