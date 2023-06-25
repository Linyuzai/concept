package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.ReactiveRedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.RedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redisson.RedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.extension.SingleThreadScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.core.extension.ScheduledExecutorServiceFactory;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapterFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.scope.ScopedFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@Configuration(proxyBeanMethods = false)
public class WebSocketLoadBalanceConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(value = "concept.websocket.load-balance.protocol", havingValue = "REDISSON_TOPIC")
    public static class RedissonTopicConfiguration {

        @Bean
        public RedissonTopicConnectionSubscriberFactory redissonTopicConnectionSubscriberFactory(RedissonClient redissonClient) {
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
        public RedissonTopicConnectionSubscriberFactory redissonTopicConnectionSubscriberFactory(RedissonClient redissonClient) {
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
        public RedisTopicConnectionSubscriberFactory redisTopicConnectionSubscriberFactory(RedisTemplate<?, ?> redisTemplate) {
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
        public ReactiveRedisTopicConnectionSubscriberFactory reactiveRedisTopicConnectionSubscriberFactory(
                ReactiveRedisTemplate<?, Object> reactiveRedisTemplate) {
            ReactiveRedisTopicConnectionSubscriberFactory factory = new ReactiveRedisTopicConnectionSubscriberFactory();
            factory.setReactiveRedisTemplate(reactiveRedisTemplate);
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    @Bean
    public SingleThreadScheduledExecutorServiceFactory wsSingleThreadScheduledExecutorServiceFactory() {
        return new SingleThreadScheduledExecutorServiceFactory()
                .addScopes(WebSocketScoped.NAME);
    }

    @Bean
    @ConditionalOnProperty(prefix = "concept.websocket.server.heartbeat",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionHeartbeatManager clientConnectionHeartbeatManager(
            WebSocketLoadBalanceProperties properties,
            List<ScheduledExecutorServiceFactory> factories) {
        long timeout = properties.getServer().getHeartbeat().getTimeout();
        long period = properties.getServer().getHeartbeat().getPeriod();
        ScheduledExecutorService service = ScopedFactory
                .create(WebSocketScoped.NAME, ScheduledExecutorService.class, factories);
        return new ConnectionHeartbeatManager(Connection.Type.CLIENT,
                timeout, period, service).addScopes(WebSocketScoped.NAME);
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
