package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RTopic;

import java.util.function.Consumer;

/**
 * Redisson 监听连接。
 * <p>
 * The connection to listen message from Redisson.
 */
@Getter
@Setter
public class RedissonTopicSubscriberConnection extends AliveForeverConnection {

    private Object id;

    private RTopic topic;

    private Integer listener;

    public RedissonTopicSubscriberConnection() {
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
