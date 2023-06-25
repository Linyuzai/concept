package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import java.util.Map;

@Getter
@Setter
public class ReactiveRedisTopicConnection extends AliveForeverConnection {

    private Object id;

    private String topic;

    private ReactiveRedisTemplate<?, Object> reactiveRedisTemplate;

    public ReactiveRedisTopicConnection(@NonNull String type) {
        super(type);
    }

    public ReactiveRedisTopicConnection(@NonNull String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    @Override
    public void doSend(Object message) {
        reactiveRedisTemplate.convertAndSend(topic, message).subscribe(l -> {

        });
    }
}
