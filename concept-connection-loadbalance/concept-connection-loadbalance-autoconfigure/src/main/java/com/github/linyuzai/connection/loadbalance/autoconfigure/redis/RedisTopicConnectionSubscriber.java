package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class RedisTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final RedisTemplate<?, ?> redisTemplate;

    @Override
    protected Connection create(String topic, String name, ConnectionLoadBalanceConcept concept) {
        RedisTopicConnection connection = new RedisTopicConnection(Connection.Type.OBSERVABLE);
        connection.setId(topic);
        connection.setTopic(topic);
        connection.setRedisTemplate(redisTemplate);
        RedisMessageListenerContainer container = newRedisMessageListenerContainer();
        MessageListener listener = (message, pattern) -> onMessageReceived(connection, message);
        connection.setCloseCallback(o -> {
            //messageListenerContainer.removeMessageListener(listener);
            if (container.isRunning()) {
                container.stop();
            }
        });
        container.addMessageListener(listener, new ChannelTopic(topic));
        container.afterPropertiesSet();
        container.start();
        return connection;
    }

    protected RedisMessageListenerContainer newRedisMessageListenerContainer() {
        RedisMessageListenerContainer messageListenerContainer = new RedisMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        return messageListenerContainer;
    }

    @Override
    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return MessageIdempotentVerifier.VERIFIED;
    }

    @Override
    protected String getExtension() {
        return "RedisTopic";
    }
}
