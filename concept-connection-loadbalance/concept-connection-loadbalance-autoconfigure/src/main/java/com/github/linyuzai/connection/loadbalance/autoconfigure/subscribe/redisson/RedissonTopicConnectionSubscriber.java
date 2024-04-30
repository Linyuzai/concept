package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.idempotent.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.net.URI;
import java.util.Map;

/**
 * Redisson (Shared) Topic 连接订阅器。
 * 通过 {@link RTopic} 转发消息，通过 {@link RTopic} 订阅消息
 * <p>
 * {@link ConnectionSubscriber} impl by Redisson (Shared) Topic.
 * forward message by {@link RTopic}, listen message by {@link RTopic}.
 */
@Getter
@RequiredArgsConstructor
public class RedissonTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final RedissonClient client;

    private final boolean shared;

    /**
     * 创建 Redisson 的监听连接。
     * <p>
     * Create the connection to listen message from Redisson.
     */
    @Override
    protected Connection createSubscriber(String id, String topic, Map<Object, Object> context,
                                          ConnectionLoadBalanceConcept concept) {
        RTopic rTopic;
        if (shared) {
            rTopic = client.getShardedTopic(topic);
        } else {
            rTopic = client.getTopic(topic);
        }
        context.put(RTopic.class, rTopic);
        RedissonTopicSubscriberConnection connection = new RedissonTopicSubscriberConnection();
        connection.setId(id);
        connection.setTopic(rTopic);
        int listener = rTopic.addListener(Object.class, (channel, object) ->
                onMessageReceived(connection, object));
        connection.setListener(listener);
        return connection;
    }

    /**
     * 创建 Redisson 的转发连接。
     * <p>
     * Create the connection to forward message by Redisson.
     */
    @Override
    protected Connection createObservable(String id, String topic, Map<Object, Object> context,
                                          ConnectionLoadBalanceConcept concept) {
        RTopic rTopic = (RTopic) context.get(RTopic.class);
        RedissonTopicObservableConnection connection = new RedissonTopicObservableConnection();
        connection.setId(id);
        connection.setClient(client);
        connection.setTopic(rTopic);
        return connection;
    }

    @Override
    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return MessageIdempotentVerifier.VERIFIED;
    }

    @Override
    protected ConnectionServer getSubscribeServer() {
        return new RedissonConnectionServer(client);
    }

    @Getter
    @RequiredArgsConstructor
    public static class RedissonConnectionServer implements ConnectionServer {

        private final RedissonClient client;

        @Override
        public String getInstanceId() {
            return null;
        }

        @Override
        public String getServiceId() {
            return "redisson";
        }

        @Override
        public String getHost() {
            //get方法用protect，不给读就算了，哼！
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
