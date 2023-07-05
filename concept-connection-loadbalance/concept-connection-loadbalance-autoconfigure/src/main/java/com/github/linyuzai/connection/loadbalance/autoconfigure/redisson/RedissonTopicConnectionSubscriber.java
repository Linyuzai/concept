package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriber;
import lombok.*;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.net.URI;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class RedissonTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final RedissonClient client;

    private final boolean shared;

    @Override
    protected Connection create(String topic, String name, Connection subscriber, ConnectionLoadBalanceConcept concept) {
        RTopic rTopic;
        if (shared) {
            rTopic = client.getShardedTopic(topic);
        } else {
            rTopic = client.getTopic(topic);
        }
        RedissonTopicConnection connection = new RedissonTopicConnection(Connection.Type.OBSERVABLE);
        connection.setId(name);
        connection.setTopic(rTopic);
        int listener = rTopic.addListener(Object.class, (channel, object) ->
                onMessageReceived(subscriber, object));
        connection.setCloseCallback(reason -> rTopic.removeListenerAsync(listener));
        return connection;
    }

    @Override
    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return MessageIdempotentVerifier.VERIFIED;
    }

    @Override
    protected ConnectionServer getSubscriberServer() {
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
