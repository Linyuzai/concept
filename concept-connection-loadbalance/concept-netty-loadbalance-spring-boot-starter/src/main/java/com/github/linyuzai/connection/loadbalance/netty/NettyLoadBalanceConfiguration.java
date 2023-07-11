package com.github.linyuzai.connection.loadbalance.netty;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.logger.ConnectionLoggerFactoryImpl;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactoryImpl;
import com.github.linyuzai.connection.loadbalance.core.extension.GroupSelector;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLoggerFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifierFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifierFactoryImpl;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapterImpl;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeLogger;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.EmptyConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyConnectionFactory;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyScoped;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class NettyLoadBalanceConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master",
            havingValue = "NONE", matchIfMissing = true)
    public static class NoneSubscriberConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = "nettyEmptyConnectionSubscriberFactory")
        public EmptyConnectionSubscriberFactory nettyEmptyConnectionSubscriberFactory() {
            return new EmptyConnectionSubscriberFactory().addScopes(NettyScoped.NAME);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master",
            havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.RedissonTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave",
            havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicSubscriberSlaveConfiguration
            extends NettySubscriberConfiguration.RedissonTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master",
            havingValue = "REDISSON_SHARED_TOPIC")
    public static class RedissonSharedTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.RedissonSharedTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave",
            havingValue = "REDISSON_SHARED_TOPIC")
    public static class RedissonSharedTopicSubscriberSlaveConfiguration
            extends NettySubscriberConfiguration.RedissonSharedTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ReactiveRedisConnectionFactory.class)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master",
            havingValue = "REDIS_TOPIC")
    public static class RedisTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.RedisTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ReactiveRedisConnectionFactory.class)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave",
            havingValue = "REDIS_TOPIC")
    public static class RedisTopicSubscriberSlaveConfiguration
            extends NettySubscriberConfiguration.RedisTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean(ReactiveRedisConnectionFactory.class)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master",
            havingValue = "REDIS_TOPIC")
    public static class ReactiveRedisTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean(ReactiveRedisConnectionFactory.class)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave",
            havingValue = "REDIS_TOPIC")
    public static class ReactiveRedisTopicSubscriberSlaveConfiguration
            extends NettySubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master",
            havingValue = "RABBIT_FANOUT")
    public static class RabbitFanoutSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.RabbitFanoutConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave",
            havingValue = "RABBIT_FANOUT")
    public static class RabbitFanoutSubscriberSlaveConfiguration
            extends NettySubscriberConfiguration.RabbitFanoutConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master",
            havingValue = "KAFKA_TOPIC")
    public static class KafkaTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.KafkaTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave",
            havingValue = "KAFKA_TOPIC")
    public static class KafkaTopicSubscriberSlaveConfiguration
            extends NettySubscriberConfiguration.KafkaTopicConfiguration
            implements ConnectionSubscriberConfiguration.SlaveProvider {
    }

    @Bean
    public NettyConnectionFactory nettyConnectionFactory() {
        return new NettyConnectionFactory();
    }

    @Bean
    public GroupSelector groupSelector() {
        return new GroupSelector().addScopes(NettyScoped.NAME);
    }

    @Bean
    @Order(100)
    public ConnectionSubscribeLogger nettyConnectionSubscribeLogger() {
        return new ConnectionSubscribeLogger().addScopes(NettyScoped.NAME);
    }

    @Bean
    public MessageRetryStrategyAdapter nettyMessageRetryStrategyAdapter(
            NettyLoadBalanceProperties properties) {
        MessageRetryStrategyAdapterImpl adapter = new MessageRetryStrategyAdapterImpl();
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
        adapter.addScopes(NettyScoped.NAME);
        return adapter;
    }

    @Bean
    public MessageIdempotentVerifierFactory nettyMessageIdempotentVerifierFactory() {
        return new MessageIdempotentVerifierFactoryImpl()
                .addScopes(NettyScoped.NAME);
    }

    @Bean
    public ScheduledExecutorFactory nettyScheduledExecutorFactory(
            NettyLoadBalanceProperties properties) {
        ScheduledExecutorFactoryImpl factory = new ScheduledExecutorFactoryImpl();
        factory.setSize(properties.getExecutor().getSize());
        factory.addScopes(NettyScoped.NAME);
        return factory;
    }

    @Bean
    public ConnectionLoggerFactory nettyConnectionLoggerFactory() {
        ConnectionLoggerFactoryImpl factory = new ConnectionLoggerFactoryImpl();
        factory.setTag("LBNetty >> ");
        factory.addScopes(NettyScoped.NAME);
        return factory;
    }

    @Bean
    @Order(200)
    @ConditionalOnProperty(value = "concept.netty.load-balance.heartbeat.enabled",
            havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager nettyLoadBalanceConnectionHeartbeatManager(
            NettyLoadBalanceProperties properties) {
        long timeout = properties.getLoadBalance().getHeartbeat().getTimeout();
        long period = properties.getLoadBalance().getHeartbeat().getPeriod();
        ConnectionHeartbeatManager manager = new ConnectionHeartbeatManager();
        manager.getConnectionTypes().add(Connection.Type.SUBSCRIBER);
        manager.getConnectionTypes().add(Connection.Type.OBSERVABLE);
        manager.setTimeout(timeout);
        manager.setPeriod(period);
        manager.addScopes(NettyScoped.NAME);
        return manager;
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public NettyLoadBalanceConcept nettyLoadBalanceConcept(
            List<ConnectionRepositoryFactory> connectionRepositoryFactories,
            List<ConnectionServerManagerFactory> connectionServerManagerFactories,
            List<ConnectionSubscriberFactory> connectionSubscriberFactories,
            List<ConnectionFactory> connectionFactories,
            List<ConnectionSelector> connectionSelectors,
            List<MessageFactory> messageFactories,
            List<MessageCodecAdapter> messageCodecAdapters,
            List<MessageRetryStrategyAdapter> messageRetryStrategyAdapters,
            List<MessageIdempotentVerifierFactory> messageIdempotentVerifierFactories,
            List<ScheduledExecutorFactory> scheduledExecutorFactories,
            List<ConnectionLoggerFactory> loggerFactories,
            List<ConnectionEventPublisherFactory> eventPublisherFactories,
            List<ConnectionEventListener> eventListeners) {
        return new NettyLoadBalanceConcept.Builder()
                .addConnectionRepositoryFactories(connectionRepositoryFactories)
                .addConnectionServerManagerFactories(connectionServerManagerFactories)
                .addConnectionSubscriberFactories(connectionSubscriberFactories)
                .addConnectionFactories(connectionFactories)
                .addConnectionSelectors(connectionSelectors)
                .addMessageFactories(messageFactories)
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
