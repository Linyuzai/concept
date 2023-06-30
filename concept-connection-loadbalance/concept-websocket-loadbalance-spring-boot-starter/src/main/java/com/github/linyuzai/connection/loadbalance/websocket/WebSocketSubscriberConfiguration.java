package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.kafka.KafkaMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.kafka.KafkaTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq.RabbitFanoutConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq.RabbitMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.ReactiveRedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.ReactiveRedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.RedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.RedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redisson.RedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.monitor.LoadBalanceMonitorLogger;
import com.github.linyuzai.connection.loadbalance.core.monitor.ScheduledConnectionLoadBalanceMonitor;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeHandler;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeLogger;
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
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;

public class WebSocketSubscriberConfiguration extends ConnectionSubscriberConfiguration {

    public interface WebSocketScopedProvider extends ConnectionSubscriberConfiguration.ScopedProvider {

        @Override
        default String getScoped() {
            return WebSocketScoped.NAME;
        }
    }

    public abstract static class RedissonTopicConfiguration
            extends ConnectionSubscriberConfiguration.RedissonTopicConfiguration
            implements WebSocketScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "wsRedissonTopicConnectionSubscriberFactory")
        public RedissonTopicConnectionSubscriberFactory wsRedissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            return redissonTopicConnectionSubscriberFactory(redissonClient);
        }
    }

    public abstract static class RedissonSharedTopicConfiguration
            extends ConnectionSubscriberConfiguration.RedissonSharedTopicConfiguration
            implements WebSocketScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "wsRedissonSharedTopicConnectionSubscriberFactory")
        public RedissonTopicConnectionSubscriberFactory wsRedissonSharedTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            return redissonSharedTopicConnectionSubscriberFactory(redissonClient);
        }
    }

    public abstract static class RedisTopicConfiguration
            extends ConnectionSubscriberConfiguration.RedisTopicConfiguration
            implements WebSocketScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "wsRedisTopicConnectionSubscriberFactory")
        public RedisTopicConnectionSubscriberFactory wsRedisTopicConnectionSubscriberFactory(
                RedisTemplate<?, ?> redisTemplate) {
            return redisTopicConnectionSubscriberFactory(redisTemplate);
        }

        @Bean
        @ConditionalOnMissingBean(name = "wsRedisMessageCodecAdapter")
        public RedisMessageCodecAdapter wsRedisMessageCodecAdapter() {
            return redisMessageCodecAdapter();
        }
    }

    public abstract static class ReactiveRedisTopicConfiguration
            extends ConnectionSubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements WebSocketScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "wsReactiveRedisTopicConnectionSubscriberFactory")
        public ReactiveRedisTopicConnectionSubscriberFactory wsReactiveRedisTopicConnectionSubscriberFactory(
                ReactiveRedisTemplate<?, Object> reactiveRedisTemplate) {
            return reactiveRedisTopicConnectionSubscriberFactory(reactiveRedisTemplate);
        }

        @Bean
        @ConditionalOnMissingBean(name = "wsReactiveRedisMessageCodecAdapter")
        public ReactiveRedisMessageCodecAdapter wsReactiveRedisMessageCodecAdapter() {
            return reactiveRedisMessageCodecAdapter();
        }
    }

    public abstract static class RabbitFanoutConfiguration
            extends ConnectionSubscriberConfiguration.RabbitFanoutConfiguration
            implements WebSocketScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "wsRabbitFanoutConnectionSubscriberFactory")
        public RabbitFanoutConnectionSubscriberFactory wsRabbitFanoutConnectionSubscriberFactory(
                RabbitTemplate rabbitTemplate,
                RabbitListenerContainerFactory<? extends org.springframework.amqp.rabbit.listener.MessageListenerContainer>
                        rabbitListenerContainerFactory) {
            return rabbitFanoutConnectionSubscriberFactory(rabbitTemplate, rabbitListenerContainerFactory);
        }

        @Bean
        @ConditionalOnMissingBean(name = "wsRabbitMessageCodecAdapter")
        public RabbitMessageCodecAdapter wsRabbitMessageCodecAdapter() {
            return rabbitMessageCodecAdapter();
        }
    }

    public abstract static class KafkaTopicConfiguration
            extends ConnectionSubscriberConfiguration.KafkaTopicConfiguration
            implements WebSocketScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "wsKafkaTopicConnectionSubscriberFactory")
        public KafkaTopicConnectionSubscriberFactory wsKafkaTopicConnectionSubscriberFactory(
                KafkaTemplate<?, Object> kafkaTemplate,
                KafkaListenerContainerFactory<? extends MessageListenerContainer>
                        kafkaListenerContainerFactory) {
            return kafkaTopicConnectionSubscriberFactory(kafkaTemplate, kafkaListenerContainerFactory);
        }

        @Bean
        @ConditionalOnMissingBean(name = "wsKafkaMessageCodecAdapter")
        public KafkaMessageCodecAdapter wsKafkaMessageCodecAdapter() {
            return kafkaMessageCodecAdapter();
        }
    }

    public abstract static class JavaxWebSocketConfiguration extends JavaxWebSocketBaseConfiguration {

        @Bean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory() {
            JavaxWebSocketConnectionSubscriberFactory factory =
                    new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            return factory;
        }
    }

    public abstract static class JavaxWebSocketSSLConfiguration extends JavaxWebSocketBaseConfiguration {

        @Bean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory() {
            JavaxWebSocketConnectionSubscriberFactory factory =
                    new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            return factory;
        }
    }

    public abstract static class ReactiveWebSocketConfiguration extends ReactiveWebSocketBaseConfiguration {

        @Bean
        public ReactiveWebSocketConnectionSubscriberFactory reactiveWebSocketConnectionSubscriberFactory() {
            ReactiveWebSocketConnectionSubscriberFactory factory =
                    new ReactiveWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            return factory;
        }
    }

    public abstract static class ReactiveWebSocketSSLConfiguration extends ReactiveWebSocketBaseConfiguration {

        @Bean
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
    }
}
