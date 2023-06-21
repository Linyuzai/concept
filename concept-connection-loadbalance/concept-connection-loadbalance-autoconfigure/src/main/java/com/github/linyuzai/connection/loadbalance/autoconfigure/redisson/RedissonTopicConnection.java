package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.util.Map;

@Getter
public class RedissonTopicConnection extends AbstractConnection {

    private final RTopic topic;

    private final RedissonClient client;

    @Setter
    private Integer listener;

    public RedissonTopicConnection(RTopic topic, RedissonClient client, String type) {
        super(type);
        this.topic = topic;
        this.client = client;
    }

    public RedissonTopicConnection(RTopic topic, RedissonClient client, String type, Map<Object, Object> metadata) {
        super(type, metadata);
        this.topic = topic;
        this.client = client;
    }

    @Override
    public void ping(PingMessage ping) {

    }

    @Override
    public void pong(PongMessage pong) {

    }

    @Override
    public void doClose(Object reason) {
        if (listener == null) {
            return;
        }
        topic.removeListenerAsync(listener);
    }

    @Override
    public Object getCloseReason(int code, String reason) {
        return reason;
    }

    @Override
    public void doSend(Object message) {
        topic.publish(message);
    }

    @Override
    public Object getId() {
        return topic;
    }

    @Override
    public void close(String reason) {
        doClose(reason);
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public long getLastHeartbeat() {
        return System.currentTimeMillis();
    }
}
