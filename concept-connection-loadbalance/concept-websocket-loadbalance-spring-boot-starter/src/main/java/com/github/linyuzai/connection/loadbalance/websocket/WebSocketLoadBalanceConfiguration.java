package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.logger.CommonsConnectionLoggerFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ThreadPoolScheduledExecutorFactory;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLoggerFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.idempotent.InMemoryMessageIdempotentVerifierFactory;
import com.github.linyuzai.connection.loadbalance.core.message.idempotent.MessageIdempotentVerifierFactory;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.retry.SimpleMessageRetryStrategyAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.sender.MessageSenderFactory;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeLogger;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.EmptyConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * ws 负载均衡配置。
 * <p>
 * ws load balance configuration.
 */
@Configuration(proxyBeanMethods = false)
public class WebSocketLoadBalanceConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "NONE")
    public static class NoneSubscriberConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = "wsEmptyConnectionSubscriberFactory")
        public EmptyConnectionSubscriberFactory wsEmptyConnectionSubscriberFactory() {
            return new EmptyConnectionSubscriberFactory().addScopes(WebSocketScoped.NAME);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.RedissonTopicConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave",
            havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicSubscriberSlaveConfiguration
            extends WebSocketSubscriberConfiguration.RedissonTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "REDISSON_TOPIC_REACTIVE")
    public static class ReactiveRedissonTopicSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.ReactiveRedissonTopicConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave",
            havingValue = "REDISSON_TOPIC_REACTIVE")
    public static class ReactiveRedissonTopicSubscriberSlaveConfiguration
            extends WebSocketSubscriberConfiguration.ReactiveRedissonTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "REDISSON_SHARED_TOPIC")
    public static class RedissonSharedTopicSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.RedissonSharedTopicConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave",
            havingValue = "REDISSON_SHARED_TOPIC")
    public static class RedissonSharedTopicSubscriberSlaveConfiguration
            extends WebSocketSubscriberConfiguration.RedissonSharedTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "REDISSON_SHARED_TOPIC_REACTIVE")
    public static class ReactiveRedissonSharedTopicSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.ReactiveRedissonSharedTopicConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave",
            havingValue = "REDISSON_SHARED_TOPIC_REACTIVE")
    public static class ReactiveRedissonSharedTopicSubscriberSlaveConfiguration
            extends WebSocketSubscriberConfiguration.ReactiveRedissonSharedTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "REDIS_TOPIC")
    public static class RedisTopicSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.RedisTopicConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave",
            havingValue = "REDIS_TOPIC")
    public static class RedisTopicSubscriberSlaveConfiguration
            extends WebSocketSubscriberConfiguration.RedisTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "REDIS_TOPIC_REACTIVE")
    public static class ReactiveRedisTopicSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave",
            havingValue = "REDIS_TOPIC_REACTIVE")
    public static class ReactiveRedisTopicSubscriberSlaveConfiguration
            extends WebSocketSubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "RABBIT_FANOUT")
    public static class RabbitFanoutSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.RabbitFanoutConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave",
            havingValue = "RABBIT_FANOUT")
    public static class RabbitFanoutSubscriberSlaveConfiguration
            extends WebSocketSubscriberConfiguration.RabbitFanoutConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master",
            havingValue = "KAFKA_TOPIC")
    public static class KafkaTopicSubscriberMasterConfiguration
            extends WebSocketSubscriberConfiguration.KafkaTopicConfiguration
            implements WebSocketSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave",
            havingValue = "KAFKA_TOPIC")
    public static class KafkaTopicSubscriberSlaveConfiguration
            extends WebSocketSubscriberConfiguration.KafkaTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Bean
    @Order(100)
    public ConnectionSubscribeLogger wsConnectionSubscribeLogger() {
        return new ConnectionSubscribeLogger().addScopes(WebSocketScoped.NAME);
    }

    @Bean
    public MessageRetryStrategyAdapter wsMessageRetryStrategyAdapter(
            WebSocketLoadBalanceProperties properties) {
        SimpleMessageRetryStrategyAdapter adapter = new SimpleMessageRetryStrategyAdapter();
        int clientTimes = properties.getServer().getMessage().getRetry().getTimes();
        int clientPeriod = properties.getServer().getMessage().getRetry().getPeriod();
        int lbTimes = properties.getLoadBalance().getMessage().getRetry().getTimes();
        int lbPeriod = properties.getLoadBalance().getMessage().getRetry().getPeriod();
        adapter.setClientMessageRetryTimes(clientTimes);
        adapter.setClientMessageRetryPeriod(clientPeriod);
        adapter.setSubscribeMessageRetryTimes(lbTimes);
        adapter.setSubscribeMessageRetryPeriod(lbPeriod);
        adapter.setForwardMessageRetryTimes(lbTimes);
        adapter.setForwardMessageRetryPeriod(lbPeriod);
        adapter.addScopes(WebSocketScoped.NAME);
        return adapter;
    }

    @Bean
    public MessageIdempotentVerifierFactory wsMessageIdempotentVerifierFactory() {
        return new InMemoryMessageIdempotentVerifierFactory()
                .addScopes(WebSocketScoped.NAME);
    }

    @Bean
    public ScheduledExecutorFactory wsScheduledExecutorFactory(
            WebSocketLoadBalanceProperties properties) {
        ThreadPoolScheduledExecutorFactory factory = new ThreadPoolScheduledExecutorFactory();
        factory.setThreadPoolSize(properties.getExecutor().getThreadPoolSize());
        factory.addScopes(WebSocketScoped.NAME);
        return factory;

    }

    @Bean
    public ConnectionLoggerFactory wsConnectionLoggerFactory() {
        CommonsConnectionLoggerFactory factory = new CommonsConnectionLoggerFactory();
        factory.setTag("LBWebSocket >> ");
        factory.addScopes(WebSocketScoped.NAME);
        return factory;
    }

    @Bean
    @Order(200)
    @ConditionalOnProperty(prefix = "concept.websocket.server.heartbeat",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager wsClientConnectionHeartbeatManager(
            WebSocketLoadBalanceProperties properties) {
        long timeout = properties.getServer().getHeartbeat().getTimeout();
        long period = properties.getServer().getHeartbeat().getPeriod();
        ConnectionHeartbeatManager manager = new ConnectionHeartbeatManager();
        manager.getConnectionTypes().add(Connection.Type.CLIENT);
        manager.setTimeout(timeout);
        manager.setPeriod(period);
        manager.addScopes(WebSocketScoped.NAME);
        return manager;
    }

    @Bean
    @Order(200)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.heartbeat.enabled",
            havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager wsLoadBalanceConnectionHeartbeatManager(
            WebSocketLoadBalanceProperties properties) {
        long timeout = properties.getLoadBalance().getHeartbeat().getTimeout();
        long period = properties.getLoadBalance().getHeartbeat().getPeriod();
        ConnectionHeartbeatManager manager = new ConnectionHeartbeatManager();
        manager.getConnectionTypes().add(Connection.Type.SUBSCRIBER);
        manager.getConnectionTypes().add(Connection.Type.OBSERVABLE);
        manager.setTimeout(timeout);
        manager.setPeriod(period);
        manager.addScopes(WebSocketScoped.NAME);
        return manager;
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
            List<MessageSenderFactory> messageSenderFactories,
            List<MessageCodecAdapter> messageCodecAdapters,
            List<MessageRetryStrategyAdapter> messageRetryStrategyAdapters,
            List<MessageIdempotentVerifierFactory> messageIdempotentVerifierFactories,
            List<ScheduledExecutorFactory> scheduledExecutorFactories,
            List<ConnectionLoggerFactory> loggerFactories,
            List<ConnectionEventPublisherFactory> eventPublisherFactories,
            List<ConnectionEventListener> eventListeners) {
        return new WebSocketLoadBalanceConcept.Builder()
                .addConnectionRepositoryFactories(connectionRepositoryFactories)
                .addConnectionServerManagerFactories(connectionServerManagerFactories)
                .addConnectionSubscriberFactories(connectionSubscriberFactories)
                .addConnectionFactories(connectionFactories)
                .addConnectionSelectors(connectionSelectors)
                .addMessageFactories(messageFactories)
                .addMessageSenderFactories(messageSenderFactories)
                .addMessageCodecAdapters(messageCodecAdapters)
                .addMessageRetryStrategyAdapters(messageRetryStrategyAdapters)
                .addMessageIdempotentVerifierFactories(messageIdempotentVerifierFactories)
                .addScheduledExecutorFactories(scheduledExecutorFactories)
                .addLoggerFactories(loggerFactories)
                .addEventPublisherFactories(eventPublisherFactories)
                .addEventListeners(eventListeners)
                .build();
    }
}
