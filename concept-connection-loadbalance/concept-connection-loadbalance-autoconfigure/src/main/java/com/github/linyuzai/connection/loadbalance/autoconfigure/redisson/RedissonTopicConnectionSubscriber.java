package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractMasterSlaveConnectionSubscriber;
import lombok.*;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

@Getter
@RequiredArgsConstructor
public class RedissonTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final RedissonClient client;

    private final boolean shared;

    @Override
    protected Connection create(String topic, String name, ConnectionLoadBalanceConcept concept) {
        RTopic rTopic;
        if (shared) {
            rTopic = client.getShardedTopic(topic);
        } else {
            rTopic = client.getTopic(topic);
        }
        RedissonTopicConnection connection = new RedissonTopicConnection(Connection.Type.OBSERVABLE);
        connection.setId(topic);
        connection.setTopic(rTopic);
        int listener = rTopic.addListener(Object.class, (channel, object) ->
                onMessageReceived(connection, object));
        connection.setCloseCallback(reason -> rTopic.removeListenerAsync(listener));
        return connection;
    }

    @Override
    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return MessageIdempotentVerifier.VERIFIED;
    }

    @Override
    protected String getExtension() {
        return "RedissonTopic";
    }
}
