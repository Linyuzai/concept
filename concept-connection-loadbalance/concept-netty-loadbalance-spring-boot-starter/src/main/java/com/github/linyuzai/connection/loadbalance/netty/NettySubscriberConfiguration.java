package com.github.linyuzai.connection.loadbalance.netty;

import com.github.linyuzai.connection.loadbalance.autoconfigure.kafka.KafkaMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.kafka.KafkaTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq.RabbitFanoutConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq.RabbitMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.ReactiveRedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.ReactiveRedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.RedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.RedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redisson.RedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
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

public class NettySubscriberConfiguration {

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
        @ConditionalOnMissingBean(name = "nettyRedissonTopicConnectionSubscriberFactory")
        public RedissonTopicConnectionSubscriberFactory nettyRedissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory =
                    new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(false);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(NettyScoped.NAME);
            return factory;
        }
    }

    public abstract static class RedissonSharedTopicConfiguration implements MasterSlaveProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyRedissonSharedTopicConnectionSubscriberFactory")
        public RedissonTopicConnectionSubscriberFactory nettyRedissonSharedTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory =
                    new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(true);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(NettyScoped.NAME);
            return factory;
        }
    }

    public abstract static class RedisTopicConfiguration implements MasterSlaveProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyRedisTopicConnectionSubscriberFactory")
        public RedisTopicConnectionSubscriberFactory nettyRedisTopicConnectionSubscriberFactory(
                RedisTemplate<?, ?> redisTemplate) {
            RedisTopicConnectionSubscriberFactory factory =
                    new RedisTopicConnectionSubscriberFactory();
            factory.setRedisTemplate(redisTemplate);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(NettyScoped.NAME);
            return factory;
        }

        @Bean
        @ConditionalOnMissingBean(name = "nettyRedisMessageCodecAdapter")
        public RedisMessageCodecAdapter nettyRedisMessageCodecAdapter() {
            return new RedisMessageCodecAdapter().addScopes(NettyScoped.NAME);
        }
    }

    public abstract static class ReactiveRedisTopicConfiguration implements MasterSlaveProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyReactiveRedisTopicConnectionSubscriberFactory")
        public ReactiveRedisTopicConnectionSubscriberFactory nettyReactiveRedisTopicConnectionSubscriberFactory(
                ReactiveRedisTemplate<?, Object> reactiveRedisTemplate) {
            ReactiveRedisTopicConnectionSubscriberFactory factory =
                    new ReactiveRedisTopicConnectionSubscriberFactory();
            factory.setReactiveRedisTemplate(reactiveRedisTemplate);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(NettyScoped.NAME);
            return factory;
        }

        @Bean
        @ConditionalOnMissingBean(name = "nettyReactiveRedisMessageCodecAdapter")
        public ReactiveRedisMessageCodecAdapter nettyReactiveRedisMessageCodecAdapter() {
            return new ReactiveRedisMessageCodecAdapter().addScopes(NettyScoped.NAME);
        }
    }

    public abstract static class RabbitFanoutConfiguration implements MasterSlaveProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyRabbitFanoutConnectionSubscriberFactory")
        public RabbitFanoutConnectionSubscriberFactory nettyRabbitFanoutConnectionSubscriberFactory(
                RabbitTemplate rabbitTemplate,
                RabbitListenerContainerFactory<? extends org.springframework.amqp.rabbit.listener.MessageListenerContainer>
                        rabbitListenerContainerFactory) {
            RabbitFanoutConnectionSubscriberFactory factory =
                    new RabbitFanoutConnectionSubscriberFactory();
            factory.setRabbitTemplate(rabbitTemplate);
            factory.setRabbitListenerContainerFactory(rabbitListenerContainerFactory);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(NettyScoped.NAME);
            return factory;
        }

        @Bean
        @ConditionalOnMissingBean(name = "nettyRabbitMessageCodecAdapter")
        public RabbitMessageCodecAdapter nettyRabbitMessageCodecAdapter() {
            return new RabbitMessageCodecAdapter().addScopes(NettyScoped.NAME);
        }
    }

    public abstract static class KafkaTopicConfiguration implements MasterSlaveProvider {

        @Bean
        @ConditionalOnMissingBean(name = "nettyKafkaTopicConnectionSubscriberFactory")
        public KafkaTopicConnectionSubscriberFactory nettyKafkaTopicConnectionSubscriberFactory(
                KafkaTemplate<?, Object> kafkaTemplate,
                KafkaListenerContainerFactory<? extends MessageListenerContainer>
                        kafkaListenerContainerFactory) {
            KafkaTopicConnectionSubscriberFactory factory =
                    new KafkaTopicConnectionSubscriberFactory();
            factory.setKafkaTemplate(kafkaTemplate);
            factory.setKafkaListenerContainerFactory(kafkaListenerContainerFactory);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(NettyScoped.NAME);
            return factory;
        }

        @Bean
        @ConditionalOnMissingBean(name = "nettyKafkaMessageCodecAdapter")
        public KafkaMessageCodecAdapter nettyKafkaMessageCodecAdapter() {
            return new KafkaMessageCodecAdapter().addScopes(NettyScoped.NAME);
        }
    }
}
