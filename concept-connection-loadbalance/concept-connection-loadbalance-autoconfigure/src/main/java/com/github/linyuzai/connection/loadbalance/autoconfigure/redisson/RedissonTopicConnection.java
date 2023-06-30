package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import lombok.Getter;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RFuture;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommands;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public class RedissonTopicConnection extends AliveForeverConnection {

    private Object id;

    private RedissonClient redissonClient;

    private RTopic topic;

    public RedissonTopicConnection(String type) {
        super(type);
    }

    public RedissonTopicConnection(String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            topic.publish(message);
            onSuccess.run();
        } catch (RedisException e) {
            onError.accept(new MessageTransportException(e));
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            Redisson redisson = (Redisson) redissonClient;
            RFuture<String> future = redisson.getCommandExecutor()
                    .readAsync((byte[]) null, StringCodec.INSTANCE, RedisCommands.PING);
            String pong = redisson.getCommandExecutor().get(future);
            if ("PONG".equalsIgnoreCase(pong)) {
                onSuccess.run();
            } else {
                onError.accept(new IllegalStateException("Redis ping: " + pong));
            }
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
