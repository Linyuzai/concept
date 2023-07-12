package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.Setter;
import reactor.core.Disposable;

import java.util.function.Consumer;

/**
 * Redisson 监听连接。
 * <p>
 * The connection to listen message form Redisson.
 */
@Getter
@Setter
public class ReactiveRedissonTopicSubscriberConnection extends AliveForeverConnection {

    private Object id;

    private Disposable disposable;

    public ReactiveRedissonTopicSubscriberConnection() {
        setType(Type.SUBSCRIBER);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    /**
     * 在连接关闭的时候停止监听。
     * <p>
     * Stop listen when closing.
     */
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
