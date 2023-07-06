package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RTopic;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public class RedissonTopicSubscriberConnection extends AliveForeverConnection {

    private Object id;

    private RTopic topic;

    private Integer listener;

    public RedissonTopicSubscriberConnection() {
        super(Type.SUBSCRIBER);
    }

    public RedissonTopicSubscriberConnection(Map<Object, Object> metadata) {
        super(Type.SUBSCRIBER, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (listener != null) {
                topic.removeListener(listener);
            }
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
