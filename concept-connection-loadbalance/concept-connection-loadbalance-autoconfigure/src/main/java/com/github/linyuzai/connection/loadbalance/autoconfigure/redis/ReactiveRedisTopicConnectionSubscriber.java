package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import reactor.core.Disposable;

@Getter
@RequiredArgsConstructor
public class ReactiveRedisTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final ReactiveRedisTemplate<?, Object> reactiveRedisTemplate;

    @Override
    protected Connection create(String topic, String name, ConnectionLoadBalanceConcept concept) {
        ReactiveRedisTopicConnection connection = new ReactiveRedisTopicConnection(Connection.Type.OBSERVABLE);
        connection.setId(topic);
        connection.setTopic(topic);
        connection.setReactiveRedisTemplate(reactiveRedisTemplate);
        Disposable disposable = reactiveRedisTemplate.listenTo(new ChannelTopic(topic)).subscribe(
                message -> onMessage(connection, getPayload(message)),
                e -> concept.onError(connection, e));
        connection.setCloseCallback(reason -> {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        });
        return connection;
    }

    protected Object getPayload(ReactiveSubscription.Message<String, Object> message) {
        return message.getMessage();
    }

    @Override
    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return MessageIdempotentVerifier.VERIFIED;
    }

    @Override
    protected String getExtension() {
        return "RedisTopicReactive";
    }
}
