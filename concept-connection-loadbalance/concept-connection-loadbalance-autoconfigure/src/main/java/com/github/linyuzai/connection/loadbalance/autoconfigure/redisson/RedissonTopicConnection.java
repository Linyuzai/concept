package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RTopic;

import java.util.Map;

@Getter
@Setter
public class RedissonTopicConnection extends AbstractConnection {

    private Object id;

    private String from;

    private RTopic topic;

    @Setter
    private Integer listener;

    public RedissonTopicConnection(String type) {
        super(type);
    }

    public RedissonTopicConnection(String type, Map<Object, Object> metadata) {
        super(type, metadata);
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
    protected void onMessagePrepared(Message message) {
        message.setFrom(from);
    }

    @Override
    public void doSend(Object message) {
        topic.publish(message);
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
