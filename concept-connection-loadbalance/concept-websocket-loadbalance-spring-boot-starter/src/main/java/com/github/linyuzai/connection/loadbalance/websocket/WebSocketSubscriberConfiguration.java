package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq.RabbitFanoutConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.ReactiveRedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.RedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redisson.RedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.ConnectionHeartbeatManager;
import com.github.linyuzai.connection.loadbalance.core.monitor.LoadBalanceMonitorLogger;
import com.github.linyuzai.connection.loadbalance.core.monitor.ScheduledConnectionLoadBalanceMonitor;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeHandler;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeLogger;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketLoadBalanceEndpoint;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketLoadBalanceHandlerMapping;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketLoadBalanceConfigurer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

public class WebSocketSubscriberConfiguration {

    public interface MasterSlaveIndexProvider {

        int getIndex();
    }

    public interface MasterIndexProvider extends MasterSlaveIndexProvider {

        @Override
        default int getIndex() {
            return ConnectionSubscriber.MasterSlave.getMasterIndex();
        }
    }

    public interface Slave1IndexProvider extends MasterSlaveIndexProvider {

        @Override
        default int getIndex() {
            return ConnectionSubscriber.MasterSlave.getSlaveIndex(1);
        }
    }

    public abstract static class RedissonTopicConfiguration implements MasterSlaveIndexProvider {

        @Bean
        @ConditionalOnMissingBean
        public RedissonTopicConnectionSubscriberFactory wsRedissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory =
                    new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(false);
            factory.setIndex(getIndex());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class RedissonSharedTopicConfiguration implements MasterSlaveIndexProvider {

        @Bean
        @ConditionalOnMissingBean
        public RedissonTopicConnectionSubscriberFactory wsRedissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory =
                    new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(true);
            factory.setIndex(getIndex());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class RedisTopicConfiguration implements MasterSlaveIndexProvider {

        @Bean
        @ConditionalOnMissingBean
        public RedisTopicConnectionSubscriberFactory wsRedisTopicConnectionSubscriberFactory(
                RedisTemplate<?, ?> redisTemplate) {
            RedisTopicConnectionSubscriberFactory factory =
                    new RedisTopicConnectionSubscriberFactory();
            factory.setRedisTemplate(redisTemplate);
            factory.setIndex(getIndex());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class ReactiveRedisTopicConfiguration implements MasterSlaveIndexProvider {

        @Bean
        @ConditionalOnMissingBean
        public ReactiveRedisTopicConnectionSubscriberFactory wsReactiveRedisTopicConnectionSubscriberFactory(
                ReactiveRedisTemplate<?, Object> reactiveRedisTemplate) {
            ReactiveRedisTopicConnectionSubscriberFactory factory =
                    new ReactiveRedisTopicConnectionSubscriberFactory();
            factory.setReactiveRedisTemplate(reactiveRedisTemplate);
            factory.setIndex(getIndex());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class RabbitFanoutConfiguration implements MasterSlaveIndexProvider {

        @Bean
        @ConditionalOnMissingBean
        public RabbitFanoutConnectionSubscriberFactory wsRabbitFanoutConnectionSubscriberFactory(
                RabbitTemplate rabbitTemplate,
                RabbitListenerContainerFactory<? extends MessageListenerContainer>
                        rabbitListenerContainerFactory) {
            RabbitFanoutConnectionSubscriberFactory factory =
                    new RabbitFanoutConnectionSubscriberFactory();
            factory.setRabbitTemplate(rabbitTemplate);
            factory.setRabbitListenerContainerFactory(rabbitListenerContainerFactory);
            factory.setIndex(getIndex());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class JavaxWebSocketConfiguration extends JavaxWebSocketBaseConfiguration
            implements MasterSlaveIndexProvider {

        @Bean
        @ConditionalOnMissingBean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory() {
            JavaxWebSocketConnectionSubscriberFactory factory =
                    new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            factory.setIndex(getIndex());
            return factory;
        }
    }

    public abstract static class JavaxWebSocketSSLConfiguration extends JavaxWebSocketBaseConfiguration
            implements MasterSlaveIndexProvider {

        @Bean
        @ConditionalOnMissingBean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory() {
            JavaxWebSocketConnectionSubscriberFactory factory =
                    new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            factory.setIndex(getIndex());
            return factory;
        }
    }

    public abstract static class ReactiveWebSocketConfiguration extends ReactiveWebSocketBaseConfiguration
            implements MasterSlaveIndexProvider {

        @Bean
        @ConditionalOnMissingBean
        public ReactiveWebSocketConnectionSubscriberFactory reactiveWebSocketConnectionSubscriberFactory() {
            ReactiveWebSocketConnectionSubscriberFactory factory =
                    new ReactiveWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            factory.setIndex(getIndex());
            return factory;
        }
    }

    public abstract static class ReactiveWebSocketSSLConfiguration extends ReactiveWebSocketBaseConfiguration
            implements MasterSlaveIndexProvider {

        @Bean
        @ConditionalOnMissingBean
        public ReactiveWebSocketConnectionSubscriberFactory reactiveWebSocketConnectionSubscriberFactory() {
            ReactiveWebSocketConnectionSubscriberFactory factory =
                    new ReactiveWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            factory.setIndex(getIndex());
            return factory;
        }
    }

    public abstract static class ServletWebSocketConfiguration extends ServletWebSocketBaseConfiguration
            implements MasterSlaveIndexProvider {

        @Bean
        public ServletWebSocketConnectionSubscriberFactory servletWebSocketConnectionSubscriberFactory() {
            ServletWebSocketConnectionSubscriberFactory factory =
                    new ServletWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            factory.setIndex(getIndex());
            return factory;
        }
    }

    public abstract static class ServletWebSocketSSLConfiguration extends ServletWebSocketBaseConfiguration
            implements MasterSlaveIndexProvider {

        @Bean
        public ServletWebSocketConnectionSubscriberFactory servletWebSocketConnectionSubscriberFactory() {
            ServletWebSocketConnectionSubscriberFactory factory =
                    new ServletWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            factory.setIndex(getIndex());
            return factory;
        }
    }

    public static class JavaxWebSocketBaseConfiguration extends WebSocketBaseConfiguration {

        @Bean
        public JavaxWebSocketLoadBalanceEndpoint javaxWebSocketLoadBalanceEndpoint(
                WebSocketLoadBalanceConcept concept) {
            concept.holdInstance();
            return new JavaxWebSocketLoadBalanceEndpoint();
        }
    }

    public static class ReactiveWebSocketBaseConfiguration extends WebSocketBaseConfiguration {

        @Bean
        public ReactiveWebSocketLoadBalanceHandlerMapping reactiveWebSocketLoadBalanceHandlerMapping(
                WebSocketLoadBalanceConcept concept) {
            return new ReactiveWebSocketLoadBalanceHandlerMapping(concept);
        }
    }

    public static class ServletWebSocketBaseConfiguration extends WebSocketBaseConfiguration {

        @Bean
        public ServletWebSocketLoadBalanceConfigurer servletWebSocketLoadBalanceConfigurer(
                WebSocketLoadBalanceConcept concept) {
            return new ServletWebSocketLoadBalanceConfigurer(concept);
        }
    }

    public static class WebSocketBaseConfiguration {

        @Bean
        public ConnectionSubscribeHandler connectionSubscribeHandler() {
            return new ConnectionSubscribeHandler().addScopes(WebSocketScoped.NAME);
        }

        @Bean
        @ConditionalOnProperty(value = "concept.websocket.load-balance.logger", havingValue = "true", matchIfMissing = true)
        public ConnectionSubscribeLogger connectionSubscribeLogger() {
            Log log = LogFactory.getLog(ConnectionSubscribeLogger.class);
            return new ConnectionSubscribeLogger(log::info, log::error)
                    .addScopes(WebSocketScoped.NAME);
        }

        @Bean
        @ConditionalOnProperty(value = "concept.websocket.load-balance.monitor.logger", havingValue = "true", matchIfMissing = true)
        public LoadBalanceMonitorLogger loadBalanceMonitorLogger() {
            Log log = LogFactory.getLog(LoadBalanceMonitorLogger.class);
            return new LoadBalanceMonitorLogger(log::info, log::error)
                    .addScopes(WebSocketScoped.NAME);
        }

        @Bean
        @ConditionalOnProperty(value = "concept.websocket.load-balance.monitor.enabled", havingValue = "true", matchIfMissing = true)
        public ScheduledConnectionLoadBalanceMonitor scheduledConnectionLoadBalanceMonitor(
                WebSocketLoadBalanceProperties properties) {
            long period = properties.getLoadBalance().getMonitor().getPeriod();
            ScheduledConnectionLoadBalanceMonitor monitor = new ScheduledConnectionLoadBalanceMonitor();
            monitor.setPeriod(period);
            monitor.addScopes(WebSocketScoped.NAME);
            return monitor;
        }

        @Bean
        @ConditionalOnProperty(value = "concept.websocket.load-balance.heartbeat.enabled", havingValue = "true", matchIfMissing = true)
        public ConnectionHeartbeatManager loadBalanceConnectionHeartbeatManager(
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
    }

}
