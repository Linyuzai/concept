package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public class RedisTopicSubscriberConnection extends AliveForeverConnection {

    private Object id;

    private RedisMessageListenerContainer container;

    public RedisTopicSubscriberConnection() {
        super(Type.SUBSCRIBER);
    }

    public RedisTopicSubscriberConnection(Map<Object, Object> metadata) {
        super(Type.SUBSCRIBER, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (container != null && container.isRunning()) {
                container.stop();
            }
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
