package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.function.Consumer;

/**
 * Redis 监听连接。
 * <p>
 * The connection to listen message from Redis.
 */
@Getter
@Setter
public class RedisTopicSubscriberConnection extends AliveForeverConnection {

    private Object id;

    private RedisMessageListenerContainer container;

    public RedisTopicSubscriberConnection() {
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
