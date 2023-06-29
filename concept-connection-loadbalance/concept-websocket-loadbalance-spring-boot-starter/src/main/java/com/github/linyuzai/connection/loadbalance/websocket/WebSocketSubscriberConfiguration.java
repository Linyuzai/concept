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

    public interface MasterSlaveProvider {

        ConnectionSubscriber.MasterSlave getMasterSlave();
    }

    public interface MasterProvider extends MasterSlaveProvider {

        @Override
        default ConnectionSubscriber.MasterSlave getMasterSlave() {
            return ConnectionSubscriber.MasterSlave.MASTER;
        }
    }

    public interface Slave1Provider extends MasterSlaveProvider {

        @Override
        default ConnectionSubscriber.MasterSlave getMasterSlave() {
            return ConnectionSubscriber.MasterSlave.SLAVE1;
        }
    }

    public abstract static class RedissonTopicConfiguration implements MasterSlaveProvider {

        @Bean
        @ConditionalOnMissingBean
        public RedissonTopicConnectionSubscriberFactory wsRedissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory =
                    new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(false);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class RedissonSharedTopicConfiguration implements MasterSlaveProvider {

        @Bean
        @ConditionalOnMissingBean
        public RedissonTopicConnectionSubscriberFactory wsRedissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory =
                    new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(true);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class RedisTopicConfiguration implements MasterSlaveProvider {

        @Bean
        @ConditionalOnMissingBean
        public RedisTopicConnectionSubscriberFactory wsRedisTopicConnectionSubscriberFactory(
                RedisTemplate<?, ?> redisTemplate) {
            RedisTopicConnectionSubscriberFactory factory =
                    new RedisTopicConnectionSubscriberFactory();
            factory.setRedisTemplate(redisTemplate);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class ReactiveRedisTopicConfiguration implements MasterSlaveProvider {

        @Bean
        @ConditionalOnMissingBean
        public ReactiveRedisTopicConnectionSubscriberFactory wsReactiveRedisTopicConnectionSubscriberFactory(
                ReactiveRedisTemplate<?, Object> reactiveRedisTemplate) {
            ReactiveRedisTopicConnectionSubscriberFactory factory =
                    new ReactiveRedisTopicConnectionSubscriberFactory();
            factory.setReactiveRedisTemplate(reactiveRedisTemplate);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class RabbitFanoutConfiguration implements MasterSlaveProvider {

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
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(WebSocketScoped.NAME);
            return factory;
        }
    }

    public abstract static class JavaxWebSocketConfiguration extends JavaxWebSocketBaseConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory() {
            JavaxWebSocketConnectionSubscriberFactory factory =
                    new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            return factory;
        }
    }

    public abstract static class JavaxWebSocketSSLConfiguration extends JavaxWebSocketBaseConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory() {
            JavaxWebSocketConnectionSubscriberFactory factory =
                    new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            return factory;
        }
    }

    public abstract static class ReactiveWebSocketConfiguration extends ReactiveWebSocketBaseConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ReactiveWebSocketConnectionSubscriberFactory reactiveWebSocketConnectionSubscriberFactory() {
            ReactiveWebSocketConnectionSubscriberFactory factory =
                    new ReactiveWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            return factory;
        }
    }

    public abstract static class ReactiveWebSocketSSLConfiguration extends ReactiveWebSocketBaseConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ReactiveWebSocketConnectionSubscriberFactory reactiveWebSocketConnectionSubscriberFactory() {
            ReactiveWebSocketConnectionSubscriberFactory factory =
                    new ReactiveWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            return factory;
        }
    }

    public abstract static class ServletWebSocketConfiguration extends ServletWebSocketBaseConfiguration {

        @Bean
        public ServletWebSocketConnectionSubscriberFactory servletWebSocketConnectionSubscriberFactory() {
            ServletWebSocketConnectionSubscriberFactory factory =
                    new ServletWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            return factory;
        }
    }

    public abstract static class ServletWebSocketSSLConfiguration extends ServletWebSocketBaseConfiguration {

        @Bean
        public ServletWebSocketConnectionSubscriberFactory servletWebSocketConnectionSubscriberFactory() {
            ServletWebSocketConnectionSubscriberFactory factory =
                    new ServletWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
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