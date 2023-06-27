package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq.RabbitFanoutConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.ReactiveRedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.RedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redisson.RedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactory;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactoryImpl;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapterImpl;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebSocketLoadBalanceConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.protocol", havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicConfiguration {

        @Bean
        public RedissonTopicConnectionSubscriberFactory wsRedissonTopicConnectionSubscriberFactory(RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory = new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(false);
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.protocol", havingValue = "REDISSON_SHARED_TOPIC")
    public static class RedissonSharedTopicConfiguration {

        @Bean
        public RedissonTopicConnectionSubscriberFactory wsRedissonTopicConnectionSubscriberFactory(RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory = new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(true);
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.protocol", havingValue = "REDIS_TOPIC")
    public static class RedisTopicConfiguration {

        @Bean
        public RedisTopicConnectionSubscriberFactory wsRedisTopicConnectionSubscriberFactory(RedisTemplate<?, ?> redisTemplate) {
            RedisTopicConnectionSubscriberFactory factory = new RedisTopicConnectionSubscriberFactory();
            factory.setRedisTemplate(redisTemplate);
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.protocol", havingValue = "REDIS_TOPIC")
    public static class ReactiveRedisTopicConfiguration {

        @Bean
        public ReactiveRedisTopicConnectionSubscriberFactory wsReactiveRedisTopicConnectionSubscriberFactory(
                ReactiveRedisTemplate<?, Object> reactiveRedisTemplate) {
            ReactiveRedisTopicConnectionSubscriberFactory factory = new ReactiveRedisTopicConnectionSubscriberFactory();
            factory.setReactiveRedisTemplate(reactiveRedisTemplate);
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.protocol", havingValue = "RABBIT_FANOUT")
    public static class RabbitFanoutConfiguration {

        @Bean
        public RabbitFanoutConnectionSubscriberFactory wsRabbitFanoutConnectionSubscriberFactory(RabbitTemplate rabbitTemplate,
                                                                                                 RabbitListenerContainerFactory<? extends MessageListenerContainer> rabbitListenerContainerFactory) {
            RabbitFanoutConnectionSubscriberFactory factory = new RabbitFanoutConnectionSubscriberFactory();
            factory.setRabbitTemplate(rabbitTemplate);
            factory.setRabbitListenerContainerFactory(rabbitListenerContainerFactory);
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    @Bean
    public MessageRetryStrategyAdapter wsMessageRetryStrategyAdapter(WebSocketLoadBalanceProperties properties) {
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
        adapter.addScopes(WebSocketScoped.NAME);
        return adapter;
    }

    @Bean
    public MessageIdempotentVerifierFactory wsMessageIdempotentVerifierFactory() {
        return new MessageIdempotentVerifierFactoryImpl()
                .addScopes(WebSocketScoped.NAME);
    }

    @Bean
    public ScheduledExecutorFactory wsScheduledExecutorFactory() {
        return new ScheduledExecutorFactoryImpl()
                .addScopes(WebSocketScoped.NAME);
    }

    @Bean
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

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public WebSocketLoadBalanceConcept webSocketLoadBalanceConcept(
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
        return new WebSocketLoadBalanceConcept.Builder()
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
                .build();
    }
}
