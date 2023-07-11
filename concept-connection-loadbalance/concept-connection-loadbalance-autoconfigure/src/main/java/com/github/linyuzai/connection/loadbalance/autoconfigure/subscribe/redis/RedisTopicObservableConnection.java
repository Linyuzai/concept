package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.function.Consumer;

/**
 * Redis 转发连接。
 * <p>
 * The connection to forward message by Redis.
 */
@Getter
@Setter
public class RedisTopicObservableConnection extends AliveForeverConnection {

    private Object id;

    private String topic;

    private StringRedisTemplate redisTemplate;

    public RedisTopicObservableConnection() {
        setType(Type.OBSERVABLE);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            redisTemplate.convertAndSend(topic, message);
            onSuccess.run();
        } catch (DataAccessException e) {
            onError.accept(new MessageTransportException(e));
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        RedisConnection connection = null;
        try {
            connection = getConnection();
            String pong = connection.ping();
            if ("PONG".equalsIgnoreCase(pong)) {
                onSuccess.run();
            } else {
                onError.accept(new IllegalStateException("Redis ping: " + pong));
            }
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable ignore) {
            } finally {
                onComplete.run();
            }
        }
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    protected RedisConnection getConnection() {
        return redisTemplate.getRequiredConnectionFactory().getConnection();
    }
}
