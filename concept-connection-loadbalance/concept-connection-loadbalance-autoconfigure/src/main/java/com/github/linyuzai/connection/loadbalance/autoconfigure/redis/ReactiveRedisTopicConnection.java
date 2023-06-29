package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import java.util.Map;
import java.util.function.Consumer;

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
}
