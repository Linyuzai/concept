package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.*;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.StatusListener;

@Getter
@RequiredArgsConstructor
public class RedissonTopicConnectionSubscriber implements ConnectionSubscriber {

    private final RedissonClient client;

    private final boolean shared;

    @Override
    public synchronized void subscribe(ConnectionLoadBalanceConcept concept) {
        ConnectionServer local = concept.getConnectionServerManager().getLocal();
        String name = getTopicName(local);
        RTopic topic;
        if (shared) {
            topic = client.getShardedTopic(name);
        } else {
            topic = client.getTopic(name);
        }
        topic.addListener(new StatusListener() {

            @Override
            public void onSubscribe(String channel) {
                System.out.println(channel);
            }

            @Override
            public void onUnsubscribe(String channel) {
                System.out.println(channel);
            }
        });
        RedissonTopicConnection connection = newConnection(topic, Connection.Type.SUBSCRIBER);
        int listener = topic.addListener(Object.class, (channel, object) -> {
            concept.onMessage(connection, object, msg -> {
                return true;
            });
            System.out.println(channel);
        });
        connection.setListener(listener);
        concept.onEstablish(connection);
        concept.onEstablish(newConnection(topic, Connection.Type.OBSERVABLE));
    }

    protected RedissonTopicConnection newConnection(RTopic topic, String type) {
        return new RedissonTopicConnection(topic, client, type);
    }

    protected String getTopicName(ConnectionServer local) {
        String p = "ConceptConnectionLB@";
        if (local == null) {
            return p + "master";
        } else {
            return p + local.getServiceId();
        }
    }
}
