package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import lombok.Getter;
import lombok.Setter;
import org.redisson.RedissonReactive;
import org.redisson.api.*;
import org.redisson.client.RedisException;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommands;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * Redisson 转发连接。
 * <p>
 * The connection to forward message by Redisson.
 */
@Getter
@Setter
public class ReactiveRedissonTopicObservableConnection extends AliveForeverConnection {

    private Object id;

    private RedissonReactiveClient client;

    private RTopicReactive topic;

    public ReactiveRedissonTopicObservableConnection() {
        setType(Type.OBSERVABLE);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        topic.publish(message).subscribe(l -> onSuccess.run(), e -> {
            if (e instanceof RedisException) {
                onError.accept(new MessageTransportException(e));
            } else {
                onError.accept(e);
            }
        }, onComplete);
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        RedissonReactive redisson = (RedissonReactive) client;
        Mono<String> mono = redisson.getCommandExecutor().reactive(() ->
                redisson.getCommandExecutor()
                        .readAsync((byte[]) null, StringCodec.INSTANCE, RedisCommands.PING));
        mono.subscribe(pong -> {
            if ("PONG".equalsIgnoreCase(pong)) {
                onSuccess.run();
            } else {
                onError.accept(new IllegalStateException("Redis ping: " + pong));
            }
        }, onError, onComplete);
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }
}
