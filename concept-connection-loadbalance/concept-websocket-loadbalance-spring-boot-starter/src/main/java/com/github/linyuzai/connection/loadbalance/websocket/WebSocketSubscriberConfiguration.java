package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka.KafkaMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka.KafkaTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq.RabbitFanoutConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq.RabbitMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.RedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.RedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.reactive.ReactiveRedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.reactive.ReactiveRedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson.RedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson.reactive.ReactiveRedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.monitor.ScheduledConnectionLoadBalanceMonitor;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeHandler;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ProtocolConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketScoped;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketLoadBalanceEndpoint;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.DefaultReactiveWebSocketClientFactory;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketClientFactory;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketLoadBalanceHandlerMapping;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.DefaultServletWebSocketClientFactory;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketClientFactory;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketLoadBalanceConfigurer;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;

/**
 * WebSocket 订阅者配置。
 * <p>
 * WebSocket subscription configuration.
 */
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

    public abstract static class ReactiveRedissonTopicConfiguration
            extends ConnectionSubscriberConfiguration.ReactiveRedissonTopicConfiguration
            implements WebSocketScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "wsReactiveRedissonTopicConnectionSubscriberFactory")
        public ReactiveRedissonTopicConnectionSubscriberFactory wsReactiveRedissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            return reactiveRedissonTopicConnectionSubscriberFactory(redissonClient);
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

    public abstract static class ReactiveRedissonSharedTopicConfiguration
            extends ConnectionSubscriberConfiguration.ReactiveRedissonSharedTopicConfiguration
            implements WebSocketScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "wsReactiveRedissonSharedTopicConnectionSubscriberFactory")
        public ReactiveRedissonTopicConnectionSubscriberFactory wsReactiveRedissonSharedTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            return reactiveRedissonTopicConnectionSubscriberFactory(redissonClient);
        }
    }

    public abstract static class RedisTopicConfiguration
            extends ConnectionSubscriberConfiguration.RedisTopicConfiguration
            implements WebSocketScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "wsRedisTopicConnectionSubscriberFactory")
        public RedisTopicConnectionSubscriberFactory wsRedisTopicConnectionSubscriberFactory(
                StringRedisTemplate redisTemplate) {
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

    @Deprecated
    public abstract static class JavaxWebSocketConfiguration extends JavaxWebSocketBaseConfiguration {

        @Bean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory(
                WebSocketLoadBalanceProperties properties) {
            JavaxWebSocketConnectionSubscriberFactory factory =
                    new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            setLoadBalanceEndpoint(factory, properties);
            return factory;
        }
    }

    @Deprecated
    public abstract static class JavaxWebSocketSSLConfiguration extends JavaxWebSocketBaseConfiguration {

        @Bean
        public JavaxWebSocketConnectionSubscriberFactory javaxWebSocketConnectionSubscriberFactory(
                WebSocketLoadBalanceProperties properties) {
            JavaxWebSocketConnectionSubscriberFactory factory =
                    new JavaxWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            setLoadBalanceEndpoint(factory, properties);
            return factory;
        }
    }

    public abstract static class ReactiveWebSocketConfiguration extends ReactiveWebSocketBaseConfiguration {

        @Bean
        public ReactiveWebSocketConnectionSubscriberFactory reactiveWebSocketConnectionSubscriberFactory(
                ReactiveWebSocketClientFactory webSocketClientFactory,
                WebSocketLoadBalanceProperties properties) {
            ReactiveWebSocketConnectionSubscriberFactory factory =
                    new ReactiveWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            factory.setWebSocketClientFactory(webSocketClientFactory);
            setLoadBalanceEndpoint(factory, properties);
            return factory;
        }
    }

    public abstract static class ReactiveWebSocketSSLConfiguration extends ReactiveWebSocketBaseConfiguration {

        @Bean
        public ReactiveWebSocketConnectionSubscriberFactory reactiveWebSocketConnectionSubscriberFactory(
                ReactiveWebSocketClientFactory webSocketClientFactory,
                WebSocketLoadBalanceProperties properties) {
            ReactiveWebSocketConnectionSubscriberFactory factory =
                    new ReactiveWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            factory.setWebSocketClientFactory(webSocketClientFactory);
            setLoadBalanceEndpoint(factory, properties);
            return factory;
        }
    }

    public abstract static class ServletWebSocketConfiguration extends ServletWebSocketBaseConfiguration {

        @Bean
        public ServletWebSocketConnectionSubscriberFactory servletWebSocketConnectionSubscriberFactory(
                ServletWebSocketClientFactory webSocketClientFactory,
                WebSocketLoadBalanceProperties properties) {
            ServletWebSocketConnectionSubscriberFactory factory =
                    new ServletWebSocketConnectionSubscriberFactory();
            factory.setProtocol("ws");
            factory.setWebSocketClientFactory(webSocketClientFactory);
            setLoadBalanceEndpoint(factory, properties);
            return factory;
        }
    }

    public abstract static class ServletWebSocketSSLConfiguration extends ServletWebSocketBaseConfiguration {

        @Bean
        public ServletWebSocketConnectionSubscriberFactory servletWebSocketConnectionSubscriberFactory(
                ServletWebSocketClientFactory webSocketClientFactory,
                WebSocketLoadBalanceProperties properties) {
            ServletWebSocketConnectionSubscriberFactory factory =
                    new ServletWebSocketConnectionSubscriberFactory();
            factory.setProtocol("wss");
            factory.setWebSocketClientFactory(webSocketClientFactory);
            setLoadBalanceEndpoint(factory, properties);
            return factory;
        }
    }

    @Deprecated
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
        @ConditionalOnMissingBean
        public ReactiveWebSocketClientFactory reactiveWebSocketClientFactory() {
            return new DefaultReactiveWebSocketClientFactory();
        }

        @Bean
        public ReactiveWebSocketLoadBalanceHandlerMapping reactiveWebSocketLoadBalanceHandlerMapping(
                WebSocketLoadBalanceConcept concept, WebSocketLoadBalanceProperties properties) {
            String endpoint = ConnectionLoadBalanceConcept.
                    formatEndpoint(properties.getLoadBalance().getObservableEndpoint());
            return new ReactiveWebSocketLoadBalanceHandlerMapping(concept, endpoint);
        }
    }

    public static class ServletWebSocketBaseConfiguration extends WebSocketBaseConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ServletWebSocketClientFactory servletWebSocketClientFactory() {
            return new DefaultServletWebSocketClientFactory();
        }

        @Bean
        public ServletWebSocketLoadBalanceConfigurer servletWebSocketLoadBalanceConfigurer(
                WebSocketLoadBalanceConcept concept, WebSocketLoadBalanceProperties properties) {
            String endpoint = ConnectionLoadBalanceConcept.
                    formatEndpoint(properties.getLoadBalance().getObservableEndpoint());
            return new ServletWebSocketLoadBalanceConfigurer(concept, endpoint);
        }
    }

    public static class WebSocketBaseConfiguration {

        protected void setLoadBalanceEndpoint(ProtocolConnectionSubscriberFactory<?> factory,
                                              WebSocketLoadBalanceProperties properties) {
            String endpoint = ConnectionLoadBalanceConcept.
                    formatEndpoint(properties.getLoadBalance().getSubscriberEndpoint());
            factory.setEndpoint(endpoint);
        }

        @Bean
        @Order(100)
        @ConditionalOnMissingBean
        public ConnectionSubscribeHandler wsConnectionSubscribeHandler() {
            return new ConnectionSubscribeHandler().addScopes(WebSocketScoped.NAME);
        }

        @Bean
        @Order(300)
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "concept.websocket.load-balance.monitor.enabled", havingValue = "true", matchIfMissing = true)
        public ScheduledConnectionLoadBalanceMonitor wsScheduledConnectionLoadBalanceMonitor(
                WebSocketLoadBalanceProperties properties) {
            long period = properties.getLoadBalance().getMonitor().getPeriod();
            boolean logger = properties.getLoadBalance().getMonitor().isLogger();
            ScheduledConnectionLoadBalanceMonitor monitor = new ScheduledConnectionLoadBalanceMonitor();
            monitor.setPeriod(period);
            monitor.setLoggerEnabled(logger);
            monitor.addScopes(WebSocketScoped.NAME);
            return monitor;
        }
    }
}
