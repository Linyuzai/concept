package com.github.linyuzai.connection.loadbalance.sse;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.logger.CommonsConnectionLoggerFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ThreadPoolScheduledExecutorFactory;
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
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseScoped;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * SSE 负载均衡配置。
 * <p>
 * SSE load balance configuration.
 */
@Configuration(proxyBeanMethods = false)
public class SseLoadBalanceConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "NONE")
    public static class NoneSubscriberConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = "sseEmptyConnectionSubscriberFactory")
        public EmptyConnectionSubscriberFactory sseEmptyConnectionSubscriberFactory() {
            return new EmptyConnectionSubscriberFactory().addScopes(SseScoped.NAME);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.RedissonTopicConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-slave",
            havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicSubscriberSlaveConfiguration
            extends SseSubscriberConfiguration.RedissonTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "REDISSON_TOPIC_REACTIVE")
    public static class ReactiveRedissonTopicSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.ReactiveRedissonTopicConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-slave",
            havingValue = "REDISSON_TOPIC_REACTIVE")
    public static class ReactiveRedissonTopicSubscriberSlaveConfiguration
            extends SseSubscriberConfiguration.ReactiveRedissonTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "REDISSON_SHARED_TOPIC")
    public static class RedissonSharedTopicSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.RedissonSharedTopicConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-slave",
            havingValue = "REDISSON_SHARED_TOPIC")
    public static class RedissonSharedTopicSubscriberSlaveConfiguration
            extends SseSubscriberConfiguration.RedissonSharedTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "REDISSON_SHARED_TOPIC_REACTIVE")
    public static class ReactiveRedissonSharedTopicSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.ReactiveRedissonSharedTopicConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-slave",
            havingValue = "REDISSON_SHARED_TOPIC_REACTIVE")
    public static class ReactiveRedissonSharedTopicSubscriberSlaveConfiguration
            extends SseSubscriberConfiguration.ReactiveRedissonSharedTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "REDIS_TOPIC")
    public static class RedisTopicSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.RedisTopicConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-slave",
            havingValue = "REDIS_TOPIC")
    public static class RedisTopicSubscriberSlaveConfiguration
            extends SseSubscriberConfiguration.RedisTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "REDIS_TOPIC_REACTIVE")
    public static class ReactiveRedisTopicSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-slave",
            havingValue = "REDIS_TOPIC_REACTIVE")
    public static class ReactiveRedisTopicSubscriberSlaveConfiguration
            extends SseSubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "RABBIT_FANOUT")
    public static class RabbitFanoutSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.RabbitFanoutConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-slave",
            havingValue = "RABBIT_FANOUT")
    public static class RabbitFanoutSubscriberSlaveConfiguration
            extends SseSubscriberConfiguration.RabbitFanoutConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-master",
            havingValue = "KAFKA_TOPIC")
    public static class KafkaTopicSubscriberMasterConfiguration
            extends SseSubscriberConfiguration.KafkaTopicConfiguration
            implements SseSubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.sse.load-balance.subscriber-slave",
            havingValue = "KAFKA_TOPIC")
    public static class KafkaTopicSubscriberSlaveConfiguration
            extends SseSubscriberConfiguration.KafkaTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Bean
    @Order(100)
    public ConnectionSubscribeLogger sseConnectionSubscribeLogger() {
        return new ConnectionSubscribeLogger().addScopes(SseScoped.NAME);
    }

    @Bean
    public MessageRetryStrategyAdapter sseMessageRetryStrategyAdapter(
            SseLoadBalanceProperties properties) {
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
        adapter.addScopes(SseScoped.NAME);
        return adapter;
    }

    @Bean
    public MessageIdempotentVerifierFactory sseMessageIdempotentVerifierFactory() {
        return new InMemoryMessageIdempotentVerifierFactory()
                .addScopes(SseScoped.NAME);
    }

    @Bean
    public ScheduledExecutorFactory sseScheduledExecutorFactory(
            SseLoadBalanceProperties properties) {
        ThreadPoolScheduledExecutorFactory factory = new ThreadPoolScheduledExecutorFactory();
        factory.setThreadPoolSize(properties.getExecutor().getThreadPoolSize());
        factory.addScopes(SseScoped.NAME);
        return factory;

    }

    @Bean
    public ConnectionLoggerFactory sseConnectionLoggerFactory() {
        CommonsConnectionLoggerFactory factory = new CommonsConnectionLoggerFactory();
        factory.setTag("LBSse >> ");
        factory.addScopes(SseScoped.NAME);
        return factory;
    }

    /*@Bean
    @Order(200)
    @ConditionalOnProperty(prefix = "concept.sse.server.heartbeat",
            name = "enabled", havingValue = "true")
    public ConnectionHeartbeatManager sseClientConnectionHeartbeatManager(
            SseLoadBalanceProperties properties) {
        long timeout = properties.getServer().getHeartbeat().getTimeout();
        long period = properties.getServer().getHeartbeat().getPeriod();
        ConnectionHeartbeatManager manager = new ConnectionHeartbeatManager();
        manager.getConnectionTypes().add(Connection.Type.CLIENT);
        manager.setTimeout(timeout);
        manager.setPeriod(period);
        manager.addScopes(SseScoped.NAME);
        return manager;
    }*/

    /*@Bean
    @Order(200)
    @ConditionalOnProperty(value = "concept.sse.load-balance.heartbeat.enabled",
            havingValue = "true")
    public ConnectionHeartbeatManager sseLoadBalanceConnectionHeartbeatManager(
            SseLoadBalanceProperties properties) {
        long timeout = properties.getLoadBalance().getHeartbeat().getTimeout();
        long period = properties.getLoadBalance().getHeartbeat().getPeriod();
        ConnectionHeartbeatManager manager = new ConnectionHeartbeatManager();
        manager.getConnectionTypes().add(Connection.Type.SUBSCRIBER);
        manager.getConnectionTypes().add(Connection.Type.OBSERVABLE);
        manager.setTimeout(timeout);
        manager.setPeriod(period);
        manager.addScopes(SseScoped.NAME);
        return manager;
    }*/

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public SseLoadBalanceConcept sseSocketLoadBalanceConcept(
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
        return new SseLoadBalanceConcept.Builder()
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
