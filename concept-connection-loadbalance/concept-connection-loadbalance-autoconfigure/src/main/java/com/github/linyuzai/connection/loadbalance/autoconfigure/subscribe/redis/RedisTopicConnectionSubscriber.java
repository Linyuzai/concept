package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class RedisTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final StringRedisTemplate redisTemplate;

    @Override
    protected Connection createSubscriber(String id, String topic, Map<Object, Object> context, ConnectionLoadBalanceConcept concept) {
        RedisTopicSubscriberConnection connection = new RedisTopicSubscriberConnection();
        connection.setId(id);
        RedisMessageListenerContainer container = createRedisMessageListenerContainer();
        MessageListener listener = (message, pattern) -> onMessageReceived(connection, message);
        container.addMessageListener(listener, new ChannelTopic(topic));
        container.afterPropertiesSet();
        container.start();
        connection.setContainer(container);
        return connection;
    }

    @Override
    protected Connection createObservable(String id, String topic, Map<Object, Object> context, ConnectionLoadBalanceConcept concept) {
        RedisTopicObservableConnection connection = new RedisTopicObservableConnection();
        connection.setId(id);
        connection.setTopic(topic);
        connection.setRedisTemplate(redisTemplate);
        return connection;
    }

    protected RedisMessageListenerContainer createRedisMessageListenerContainer() {
        RedisMessageListenerContainer messageListenerContainer = new RedisMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        return messageListenerContainer;
    }

    @Override
    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return MessageIdempotentVerifier.VERIFIED;
    }

    @Override
    protected ConnectionServer getSubscribeServer() {
        return new RedisConnectionServer(redisTemplate);
    }

    @Getter
    @RequiredArgsConstructor
    public static class RedisConnectionServer implements ConnectionServer {

        private final StringRedisTemplate redisTemplate;

        @Override
        public String getInstanceId() {
            return null;
        }

        @Override
        public String getServiceId() {
            return "redis";
        }

        @Override
        public String getHost() {
            return null;
        }

        @Override
        public int getPort() {
            return 0;
        }

        @Override
        public Map<String, String> getMetadata() {
            return null;
        }

        @Override
        public URI getUri() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }
    }
}
