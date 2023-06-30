package com.github.linyuzai.connection.loadbalance.netty;

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
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyScoped;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
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

    public abstract static class RedisTopicConfiguration
            extends ConnectionSubscriberConfiguration.RedisTopicConfiguration
            implements NettyScopedProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyRedisTopicConnectionSubscriberFactory")
        public RedisTopicConnectionSubscriberFactory nettyRedisTopicConnectionSubscriberFactory(
                RedisTemplate<?, ?> redisTemplate) {
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
