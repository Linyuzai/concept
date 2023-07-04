package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.autoconfigure.kafka.KafkaMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.kafka.KafkaTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq.RabbitFanoutConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq.RabbitMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.ReactiveRedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.ReactiveRedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.RedisMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redis.RedisTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.autoconfigure.redisson.RedissonTopicConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlave;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class ConnectionSubscriberConfiguration {

    public interface MasterSlaveProvider {

        MasterSlave getMasterSlave();
    }

    public interface MasterProvider extends MasterSlaveProvider {

        @Override
        default MasterSlave getMasterSlave() {
            return MasterSlave.MASTER;
        }
    }

    public interface Slave1Provider extends MasterSlaveProvider {

        @Override
        default MasterSlave getMasterSlave() {
            return MasterSlave.SLAVE1;
        }
    }

    public interface ScopedProvider {

        String getScoped();
    }

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

    public abstract static class RedisTopicConfiguration
            implements ScopedProvider, MasterSlaveProvider {

        public RedisTopicConnectionSubscriberFactory redisTopicConnectionSubscriberFactory(
                RedisTemplate<?, ?> redisTemplate) {
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
