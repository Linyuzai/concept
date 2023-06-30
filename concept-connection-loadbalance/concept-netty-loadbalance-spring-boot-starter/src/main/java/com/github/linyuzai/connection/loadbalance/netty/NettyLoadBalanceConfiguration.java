package com.github.linyuzai.connection.loadbalance.netty;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactoryImpl;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifierFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifierFactoryImpl;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapterImpl;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyScoped;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class NettyLoadBalanceConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master", havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.RedissonTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave1", havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicSubscriberSlave1Configuration
            extends NettySubscriberConfiguration.RedissonTopicConfiguration
            implements NettySubscriberConfiguration.Slave1Provider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master", havingValue = "REDISSON_SHARED_TOPIC")
    public static class RedissonSharedTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.RedissonSharedTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave1", havingValue = "REDISSON_SHARED_TOPIC")
    public static class RedissonSharedTopicSubscriberSlave1Configuration
            extends NettySubscriberConfiguration.RedissonSharedTopicConfiguration
            implements NettySubscriberConfiguration.Slave1Provider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master", havingValue = "REDIS_TOPIC")
    public static class RedisTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.RedisTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave1", havingValue = "REDIS_TOPIC")
    public static class RedisTopicSubscriberSlave1Configuration
            extends NettySubscriberConfiguration.RedisTopicConfiguration
            implements NettySubscriberConfiguration.Slave1Provider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master", havingValue = "REDIS_TOPIC")
    public static class ReactiveRedisTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave1", havingValue = "REDIS_TOPIC")
    public static class ReactiveRedisTopicSubscriberSlave1Configuration
            extends NettySubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements NettySubscriberConfiguration.Slave1Provider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master", havingValue = "RABBIT_FANOUT")
    public static class RabbitFanoutSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.RabbitFanoutConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave1", havingValue = "RABBIT_FANOUT")
    public static class RabbitFanoutSubscriberSlave1Configuration
            extends NettySubscriberConfiguration.RabbitFanoutConfiguration
            implements NettySubscriberConfiguration.Slave1Provider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-master", havingValue = "KAFKA_TOPIC")
    public static class KafkaTopicSubscriberMasterConfiguration
            extends NettySubscriberConfiguration.KafkaTopicConfiguration
            implements NettySubscriberConfiguration.MasterProvider {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.netty.load-balance.subscriber-slave1", havingValue = "KAFKA_TOPIC")
    public static class KafkaTopicSubscriberSlave1Configuration
            extends NettySubscriberConfiguration.KafkaTopicConfiguration
            implements NettySubscriberConfiguration.Slave1Provider {
    }

    @Bean
    public MessageRetryStrategyAdapter nettyMessageRetryStrategyAdapter(NettyLoadBalanceProperties properties) {
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
    public ScheduledExecutorFactory nettyScheduledExecutorFactory() {
        return new ScheduledExecutorFactoryImpl()
                .addScopes(NettyScoped.NAME);
    }

    @Bean
    @ConditionalOnProperty(value = "concept.netty.load-balance.heartbeat.enabled",
            havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager nettyLoadBalanceConnectionHeartbeatManager(
            NettyLoadBalanceProperties properties) {
        long timeout = properties.getLoadBalance().getHeartbeat().getTimeout();
        long period = properties.getLoadBalance().getHeartbeat().getPeriod();
        ConnectionHeartbeatManager manager = new ConnectionHeartbeatManager();
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
                .addEventPublisherFactories(eventPublisherFactories)
                .addEventListeners(eventListeners)
                .addScheduledExecutorFactories(scheduledExecutorFactories)
                .snapshot()
                .build();
    }
}
