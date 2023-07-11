package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import java.util.function.Consumer;

/**
 * Reactive Redis 转发连接。
 * <p>
 * The connection to forward message by Reactive Redis.
 */
@Getter
@Setter
public class ReactiveRedisTopicObservableConnection extends AliveForeverConnection {

    private Object id;

    private String topic;

    private ReactiveRedisTemplate<?, Object> reactiveRedisTemplate;

    public ReactiveRedisTopicObservableConnection() {
        setType(Type.OBSERVABLE);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        reactiveRedisTemplate.convertAndSend(topic, message)
                .subscribe(l -> onSuccess.run(), e -> {
                    if (e instanceof DataAccessException) {
                        onError.accept(new MessageTransportException(e));
                    } else {
                        onError.accept(e);
                    }
                }, onComplete);
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            ReactiveRedisConnection connection = getConnection();

            connection.ping().subscribe(pong -> {
                if ("PONG".equalsIgnoreCase(pong)) {
                    onSuccess.run();
                } else {
                    onError.accept(new IllegalStateException("Redis ping: " + pong));
                }
            }, onError, () -> connection.closeLater().subscribe(v -> {
            }, e -> {
            }, onComplete));
        } catch (Throwable ignore) {
            onComplete.run();
        }
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    protected ReactiveRedisConnection getConnection() {
        return reactiveRedisTemplate.getConnectionFactory().getReactiveConnection();
    }
}
