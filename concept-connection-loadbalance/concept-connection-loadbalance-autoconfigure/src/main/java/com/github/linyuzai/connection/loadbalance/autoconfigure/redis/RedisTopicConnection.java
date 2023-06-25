package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

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
    public void doSend(Object message) {
        redisTemplate.convertAndSend(topic, message);
    }
}
