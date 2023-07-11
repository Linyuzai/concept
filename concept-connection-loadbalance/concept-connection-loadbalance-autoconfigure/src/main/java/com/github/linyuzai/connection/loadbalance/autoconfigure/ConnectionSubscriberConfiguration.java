package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka.KafkaMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka.KafkaTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq.RabbitFanoutConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq.RabbitMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.ReactiveRedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.ReactiveRedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.RedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.RedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson.RedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlave;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * 连接订阅器配置复用。
 * <p>
 * Configuration of connection subscriber to reuse.
 */
public class ConnectionSubscriberConfiguration {

    /**
     * 主从提供器。
     * <p>
     * Provider of master slave config.
     */
    public interface MasterSlaveProvider {

        MasterSlave getMasterSlave();
    }

    /**
     * 主连接订阅器。
     * <p>
     * Provider of master config.
     */
    public interface MasterProvider extends MasterSlaveProvider {

        @Override
        default MasterSlave getMasterSlave() {
            return MasterSlave.MASTER;
        }
    }

    /**
     * 从连接订阅器。
     * <p>
     * Provider of slave config.
     */
    public interface SlaveProvider extends MasterSlaveProvider {

        @Override
        default MasterSlave getMasterSlave() {
            return MasterSlave.SLAVE;
        }
    }

    /**
     * 连接域提供器。
     * <p>
     * Provider of connection's scope.
     */
    public interface ScopedProvider {

        String getScoped();
    }

    /**
     * Redisson Topic 复用配置。
     * <p>
     * Configuration of redisson's topic subscriber to reuse.
     */
    public abstract static class RedissonTopicConfiguration
            implements ScopedProvider, MasterSlaveProvider {

        public RedissonTopicConnectionSubscriberFactory redissonTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory =
                    new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(false);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(getScoped());
            return factory;
        }
    }

    /**
     * Redisson Shared Topic 复用配置。
     * <p>
     * Configuration of redisson's shared topic subscriber to reuse.
     */
    public abstract static class RedissonSharedTopicConfiguration
            implements ScopedProvider, MasterSlaveProvider {

        public RedissonTopicConnectionSubscriberFactory redissonSharedTopicConnectionSubscriberFactory(
                RedissonClient redissonClient) {
            RedissonTopicConnectionSubscriberFactory factory =
                    new RedissonTopicConnectionSubscriberFactory();
            factory.setRedissonClient(redissonClient);
            factory.setShared(true);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(getScoped());
            return factory;
        }
    }

    /**
     * Redis Topic 复用配置。
     * <p>
     * Configuration of redis's topic subscriber to reuse.
     */
    public abstract static class RedisTopicConfiguration
            implements ScopedProvider, MasterSlaveProvider {

        public RedisTopicConnectionSubscriberFactory redisTopicConnectionSubscriberFactory(
                StringRedisTemplate redisTemplate) {
            RedisTopicConnectionSubscriberFactory factory =
                    new RedisTopicConnectionSubscriberFactory();
            factory.setRedisTemplate(redisTemplate);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(getScoped());
            return factory;
        }

        public RedisMessageCodecAdapter redisMessageCodecAdapter() {
            return new RedisMessageCodecAdapter().addScopes(getScoped());
        }
    }

    /**
     * Reactive Redis Topic 复用配置。
     * <p>
     * Configuration of reactive redis's subscriber to reuse.
     */
    public abstract static class ReactiveRedisTopicConfiguration
            implements ScopedProvider, MasterSlaveProvider {

        public ReactiveRedisTopicConnectionSubscriberFactory reactiveRedisTopicConnectionSubscriberFactory(
                ReactiveRedisTemplate<?, Object> reactiveRedisTemplate) {
            ReactiveRedisTopicConnectionSubscriberFactory factory =
                    new ReactiveRedisTopicConnectionSubscriberFactory();
            factory.setReactiveRedisTemplate(reactiveRedisTemplate);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(getScoped());
            return factory;
        }

        public ReactiveRedisMessageCodecAdapter reactiveRedisMessageCodecAdapter() {
            return new ReactiveRedisMessageCodecAdapter().addScopes(getScoped());
        }
    }

    /**
     * Rabbit Fanout Exchange 复用配置。
     * <p>
     * Configuration of rabbitmq's subscriber to reuse.
     */
    public abstract static class RabbitFanoutConfiguration
            implements ScopedProvider, MasterSlaveProvider {

        public RabbitFanoutConnectionSubscriberFactory rabbitFanoutConnectionSubscriberFactory(
                RabbitTemplate rabbitTemplate,
                RabbitListenerContainerFactory<? extends MessageListenerContainer>
                        rabbitListenerContainerFactory) {
            RabbitFanoutConnectionSubscriberFactory factory =
                    new RabbitFanoutConnectionSubscriberFactory();
            factory.setRabbitTemplate(rabbitTemplate);
            factory.setRabbitListenerContainerFactory(rabbitListenerContainerFactory);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(getScoped());
            return factory;
        }

        public RabbitMessageCodecAdapter rabbitMessageCodecAdapter() {
            return new RabbitMessageCodecAdapter().addScopes(getScoped());
        }
    }

    /**
     * Kafka Topic 复用配置。
     * <p>
     * Configuration of kafka's subscriber to reuse.
     */
    public abstract static class KafkaTopicConfiguration
            implements ScopedProvider, MasterSlaveProvider {

        public KafkaTopicConnectionSubscriberFactory kafkaTopicConnectionSubscriberFactory(
                KafkaTemplate<?, Object> kafkaTemplate,
                KafkaListenerContainerFactory<? extends org.springframework.kafka.listener.MessageListenerContainer>
                        kafkaListenerContainerFactory) {
            KafkaTopicConnectionSubscriberFactory factory =
                    new KafkaTopicConnectionSubscriberFactory();
            factory.setKafkaTemplate(kafkaTemplate);
            factory.setKafkaListenerContainerFactory(kafkaListenerContainerFactory);
            factory.setMasterSlave(getMasterSlave());
            factory.addScopes(getScoped());
            return factory;
        }

        public KafkaMessageCodecAdapter kafkaMessageCodecAdapter() {
            return new KafkaMessageCodecAdapter().addScopes(getScoped());
        }
    }
}
