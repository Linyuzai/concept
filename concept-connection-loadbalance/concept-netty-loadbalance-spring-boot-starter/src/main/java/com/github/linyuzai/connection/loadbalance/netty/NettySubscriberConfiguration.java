package com.github.linyuzai.connection.loadbalance.netty;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionSubscriberConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka.KafkaMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka.KafkaTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq.RabbitFanoutConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq.RabbitMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.reactive.ReactiveRedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.reactive.ReactiveRedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.RedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.RedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson.RedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson.reactive.ReactiveRedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyScoped;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;

public class NettySubscriberConfiguration extends ConnectionSubscriberConfiguration {

    public interface NettyScopedProvider extends ConnectionSubscriberConfiguration.ScopedProvider {

        @Override
        default String getScoped() {
            return NettyScoped.NAME;
        }
    }

    public abstract static class RedissonTopicConfiguration
            extends ConnectionSubscriberConfiguration.RedissonTopicConfiguration
            implements NettyScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyRedissonTopicConnectionSubscriberFactory")
        public RedissonTopicConnectionSubscriberFactory nettyRedissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            return redissonTopicConnectionSubscriberFactory(redissonClient);
        }
    }

    public abstract static class ReactiveRedissonTopicConfiguration
            extends ConnectionSubscriberConfiguration.ReactiveRedissonTopicConfiguration
            implements NettyScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyReactiveRedissonTopicConnectionSubscriberFactory")
        public ReactiveRedissonTopicConnectionSubscriberFactory nettyReactiveRedissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            return reactiveRedissonTopicConnectionSubscriberFactory(redissonClient);
        }
    }

    public abstract static class RedissonSharedTopicConfiguration
            extends ConnectionSubscriberConfiguration.RedissonSharedTopicConfiguration
            implements NettyScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyRedissonSharedTopicConnectionSubscriberFactory")
        public RedissonTopicConnectionSubscriberFactory nettyRedissonSharedTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            return redissonSharedTopicConnectionSubscriberFactory(redissonClient);
        }
    }

    public abstract static class ReactiveRedissonSharedTopicConfiguration
            extends ConnectionSubscriberConfiguration.ReactiveRedissonSharedTopicConfiguration
            implements NettyScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyReactiveRedissonSharedTopicConnectionSubscriberFactory")
        public ReactiveRedissonTopicConnectionSubscriberFactory nettyReactiveRedissonSharedTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            return reactiveRedissonTopicConnectionSubscriberFactory(redissonClient);
        }
    }

    public abstract static class RedisTopicConfiguration
            extends ConnectionSubscriberConfiguration.RedisTopicConfiguration
            implements NettyScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyRedisTopicConnectionSubscriberFactory")
        public RedisTopicConnectionSubscriberFactory nettyRedisTopicConnectionSubscriberFactory(
                StringRedisTemplate redisTemplate) {
            return redisTopicConnectionSubscriberFactory(redisTemplate);
        }

        @Bean
        @ConditionalOnMissingBean(name = "nettyRedisMessageCodecAdapter")
        public RedisMessageCodecAdapter nettyRedisMessageCodecAdapter() {
            return redisMessageCodecAdapter();
        }
    }

    public abstract static class ReactiveRedisTopicConfiguration
            extends ConnectionSubscriberConfiguration.ReactiveRedisTopicConfiguration
            implements NettyScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyReactiveRedisTopicConnectionSubscriberFactory")
        public ReactiveRedisTopicConnectionSubscriberFactory nettyReactiveRedisTopicConnectionSubscriberFactory(
                ReactiveRedisTemplate<?, Object> reactiveRedisTemplate) {
            return reactiveRedisTopicConnectionSubscriberFactory(reactiveRedisTemplate);
        }

        @Bean
        @ConditionalOnMissingBean(name = "nettyReactiveRedisMessageCodecAdapter")
        public ReactiveRedisMessageCodecAdapter nettyReactiveRedisMessageCodecAdapter() {
            return reactiveRedisMessageCodecAdapter();
        }
    }

    public abstract static class RabbitFanoutConfiguration
            extends ConnectionSubscriberConfiguration.RabbitFanoutConfiguration
            implements NettyScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyRabbitFanoutConnectionSubscriberFactory")
        public RabbitFanoutConnectionSubscriberFactory nettyRabbitFanoutConnectionSubscriberFactory(
                RabbitTemplate rabbitTemplate,
                RabbitListenerContainerFactory<? extends org.springframework.amqp.rabbit.listener.MessageListenerContainer>
                        rabbitListenerContainerFactory) {
            return rabbitFanoutConnectionSubscriberFactory(rabbitTemplate, rabbitListenerContainerFactory);
        }

        @Bean
        @ConditionalOnMissingBean(name = "nettyRabbitMessageCodecAdapter")
        public RabbitMessageCodecAdapter nettyRabbitMessageCodecAdapter() {
            return rabbitMessageCodecAdapter();
        }
    }

    public abstract static class KafkaTopicConfiguration
            extends ConnectionSubscriberConfiguration.KafkaTopicConfiguration
            implements NettyScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyKafkaTopicConnectionSubscriberFactory")
        public KafkaTopicConnectionSubscriberFactory nettyKafkaTopicConnectionSubscriberFactory(
                KafkaTemplate<?, Object> kafkaTemplate,
                KafkaListenerContainerFactory<? extends MessageListenerContainer>
                        kafkaListenerContainerFactory) {
            return kafkaTopicConnectionSubscriberFactory(kafkaTemplate, kafkaListenerContainerFactory);
        }

        @Bean
        @ConditionalOnMissingBean(name = "nettyKafkaMessageCodecAdapter")
        public KafkaMessageCodecAdapter nettyKafkaMessageCodecAdapter() {
            return kafkaMessageCodecAdapter();
        }
    }
}
