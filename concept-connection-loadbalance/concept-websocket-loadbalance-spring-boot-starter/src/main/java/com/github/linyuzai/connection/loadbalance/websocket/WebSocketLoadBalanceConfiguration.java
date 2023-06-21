package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.discovery.DiscoveryConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.extension.SampleThreadScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.core.extension.ScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapterFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactoryImpl;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeHandler;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebSocketLoadBalanceConfiguration {

    @Bean
    public ConnectionSubscribeHandler connectionSubscribeHandler() {
        return new ConnectionSubscribeHandler();
    }

    @ConditionalOnClass({DiscoveryClient.class, Registration.class})
    @Configuration(proxyBeanMethods = false)
    public static class DiscoveryConnectionServerManagerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ConnectionServerManagerFactory connectionServerManagerFactory(DiscoveryClient client,
                                                                             Registration registration) {
            return new DiscoveryConnectionServerManagerFactory(client, registration)
                    .addScopes(WebSocketScoped.NAME);
        }
    }

    @ConditionalOnMissingClass({
            "org.springframework.cloud.client.discovery.DiscoveryClient",
            "org.springframework.cloud.client.serviceregistry.Registration"})
    @Configuration(proxyBeanMethods = false)
    public static class ImplConnectionServerManagerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ConnectionServerManagerFactory connectionServerManagerFactory() {
            return new ConnectionServerManagerFactoryImpl()
                    .addScopes(WebSocketScoped.NAME);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduledExecutorServiceFactory scheduledExecutorServiceFactory() {
        return new SampleThreadScheduledExecutorServiceFactory();
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.server.heartbeat",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager clientConnectionHeartbeatManager(
            WebSocketLoadBalanceProperties properties,
            ScheduledExecutorServiceFactory factory) {
        WebSocketLoadBalanceProperties.HeartbeatProperties heartbeat = properties.getServer().getHeartbeat();
        return new ConnectionHeartbeatManager(Connection.Type.CLIENT,
                heartbeat.getTimeout(), heartbeat.getPeriod(),
                factory.create(WebSocketScoped.NAME));
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public WebSocketLoadBalanceConcept webSocketLoadBalanceConcept(
            List<ConnectionRepositoryFactory> connectionRepositoryFactories,
            List<ConnectionServerManagerFactory> connectionServerManagerFactories,
            List<ConnectionSubscriberFactory> connectionSubscriberFactories,
            List<ConnectionFactory> connectionFactories,
            List<ConnectionSelector> connectionSelectors,
            List<MessageFactory> messageFactories,
            List<MessageCodecAdapterFactory> messageCodecAdapterFactories,
            List<ConnectionEventPublisherFactory> eventPublisherFactories,
            List<ConnectionEventListener> eventListeners) {
        return new WebSocketLoadBalanceConcept.Builder()
                .addConnectionRepositoryFactories(connectionRepositoryFactories)
                .addConnectionServerManagerFactories(connectionServerManagerFactories)
                .addConnectionSubscriberFactories(connectionSubscriberFactories)
                .addConnectionFactories(connectionFactories)
                .addConnectionSelectors(connectionSelectors)
                .addMessageFactories(messageFactories)
                .addMessageCodecAdapterFactories(messageCodecAdapterFactories)
                .addEventPublisherFactories(eventPublisherFactories)
                .addEventListeners(eventListeners)
                .build();
    }
}
