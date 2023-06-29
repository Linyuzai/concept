package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.ClusterInfo;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

@Getter
@Setter
public class RedisTopicConnection extends AliveForeverConnection {

    private Object id;

    private String topic;

    private RedisTemplate<?, ?> redisTemplate;

    public RedisTopicConnection(@NonNull String type) {
        super(type);
    }

    public RedisTopicConnection(@NonNull String type, Map<Object, Object> metadata) {
        super(type, metadata);
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
    public void doPing(PingMessage message, Runnable success, Consumer<Throwable> error, Runnable onComplete) {
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        if (factory == null) {
            return;
        }
        RedisConnection connection = RedisConnectionUtils.getConnection(factory);
        try {
            if (connection instanceof RedisClusterConnection) {
                ClusterInfo info = ((RedisClusterConnection) connection).clusterGetClusterInfo();
            } else {
                Properties info = connection.info("server");
            }
            //TODO
        } finally {
            RedisConnectionUtils.releaseConnection(connection, factory);
        }
    }
}
