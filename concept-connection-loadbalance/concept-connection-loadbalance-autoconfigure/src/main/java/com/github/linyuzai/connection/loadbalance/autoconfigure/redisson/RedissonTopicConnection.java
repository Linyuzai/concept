package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RTopic;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public class RedissonTopicConnection extends AliveForeverConnection {

    private Object id;

    private RTopic topic;

    public RedissonTopicConnection(String type) {
        super(type);
    }

    public RedissonTopicConnection(String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    @Override
    public void doSend(Object message, Runnable success, Consumer<Throwable> error) {
        try {
            topic.publish(message);
            success.run();
        } catch (Throwable e) {
            error.accept(new MessageTransportException(e));
        }
    }
}
