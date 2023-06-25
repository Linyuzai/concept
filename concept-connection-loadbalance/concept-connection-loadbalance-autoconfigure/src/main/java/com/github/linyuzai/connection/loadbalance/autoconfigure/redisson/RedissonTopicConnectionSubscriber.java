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

    public static final String PREFIX = "ConceptConnectionLB@";

    private final RedissonClient client;

    private final boolean shared;

    @Override
    public synchronized void subscribe(ConnectionLoadBalanceConcept concept) {
        ConnectionServer local = concept.getConnectionServerManager().getLocal();
        String id = getId(local);
        RTopic topic;
        if (shared) {
            topic = client.getShardedTopic(id);
        } else {
            topic = client.getTopic(id);
        }
        topic.addListener(new StatusListener() {

            @Override
            public void onSubscribe(String channel) {
                System.out.println("onSubscribe:" + channel);
            }

            @Override
            public void onUnsubscribe(String channel) {
                System.out.println("onUnsubscribe:" + channel);
            }
        });

        String from = getFrom(local);

        RedissonTopicConnection connection = new RedissonTopicConnection(Connection.Type.OBSERVABLE);
        connection.setId(id);
        connection.setFrom(from);
        connection.setTopic(topic);
        int listener = topic.addListener(Object.class, (channel, object) -> {
            System.out.println("onMessage:" + channel + "," + object);
            concept.onMessage(connection, object, msg -> !from.equals(msg.getFrom()));
        });
        connection.setListener(listener);
        concept.onEstablish(connection);
    }

    protected String getFrom(ConnectionServer local) {
        return local == null ? useUnknownIfNull(null) : useUnknownIfNull(local.getInstanceId());
    }

    protected String getId(ConnectionServer local) {
        if (local == null) {
            return PREFIX + useUnknownIfNull(null);
        } else {
            return PREFIX + useUnknownIfNull(local.getServiceId());
        }
    }

    protected String useUnknownIfNull(String s) {
        if (s == null) {
            return "Unknown";
        }
        return s;
    }
}
