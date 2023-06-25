package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriber;
import lombok.*;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

@Getter
@RequiredArgsConstructor
public class RedissonTopicConnectionSubscriber extends AbstractConnectionSubscriber {

    private final RedissonClient client;

    private final boolean shared;

    @Override
    protected Connection create(String topic, ConnectionLoadBalanceConcept concept) {
        RTopic rTopic;
        if (shared) {
            rTopic = client.getShardedTopic(topic);
        } else {
            rTopic = client.getTopic(topic);
        }
        RedissonTopicConnection connection = new RedissonTopicConnection(Connection.Type.OBSERVABLE);
        connection.setId(topic);
        connection.setTopic(rTopic);
        int listener = rTopic.addListener(Object.class, (channel, object) -> {
            System.out.println("onMessage:" + channel + "," + object);
            onMessage(connection, object);
        });
        connection.setCloseCallback(reason -> rTopic.removeListenerAsync(listener));
        return connection;
    }

    @Override
    protected String getExtension() {
        return "RedissonTopic";
    }
}
