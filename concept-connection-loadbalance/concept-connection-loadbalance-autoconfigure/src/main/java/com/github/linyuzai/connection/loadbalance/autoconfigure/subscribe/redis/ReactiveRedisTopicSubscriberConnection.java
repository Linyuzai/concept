package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.Setter;
import reactor.core.Disposable;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public class ReactiveRedisTopicSubscriberConnection extends AliveForeverConnection {

    private Object id;

    private Disposable disposable;

    public ReactiveRedisTopicSubscriberConnection() {
        super(Type.SUBSCRIBER);
    }

    public ReactiveRedisTopicSubscriberConnection(Map<Object, Object> metadata) {
        super(Type.SUBSCRIBER, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
