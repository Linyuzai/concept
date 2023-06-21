package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.*;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.StatusListener;
import org.springframework.cloud.client.serviceregistry.Registration;

@Getter
@RequiredArgsConstructor
public class RedissonTopicConnectionSubscriber implements ConnectionSubscriber {

    private final RedissonClient client;

    private final Registration registration;

    private final boolean shared;

    @Override
    public synchronized void subscribe(ConnectionLoadBalanceConcept concept) {
        String name = "ConceptConnectionLB@" + registration.getServiceId();
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
        int listener = topic.addListener(Object.class, (channel, msg) -> {
            concept.onMessage(connection, msg);
            System.out.println(channel);
        });
        connection.setListener(listener);
        concept.onEstablish(connection);
        concept.onEstablish(newConnection(topic, Connection.Type.OBSERVABLE));
    }

    protected RedissonTopicConnection newConnection(RTopic topic, String type) {
        return new RedissonTopicConnection(topic, client, type);
    }
}
