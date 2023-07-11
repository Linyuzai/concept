package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import reactor.core.Disposable;

import java.net.URI;
import java.util.Map;

/**
 * Reactive Redis Topic 连接订阅器。
 * 通过 {@link ReactiveRedisTemplate} 转发消息，通过 {@link ReactiveRedisTemplate} 订阅消息
 * <p>
 * {@link ConnectionSubscriber} impl by Reactive Redis Topic.
 * forward message by {@link ReactiveRedisTemplate}, listen message by {@link ReactiveRedisTemplate}.
 */
@Getter
@RequiredArgsConstructor
public class ReactiveRedisTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final ReactiveRedisTemplate<?, Object> reactiveRedisTemplate;

    /**
     * 创建 Reactive Redis 的监听连接。
     * <p>
     * Create the connection to listen message from Reactive Redis.
     */
    @Override
    protected Connection createSubscriber(String id, String topic, Map<Object, Object> context, ConnectionLoadBalanceConcept concept) {
        ReactiveRedisTopicSubscriberConnection connection = new ReactiveRedisTopicSubscriberConnection();
        connection.setId(id);
        Disposable disposable = reactiveRedisTemplate.listenTo(new ChannelTopic(topic)).subscribe(
                message -> onMessageReceived(connection, message),
                e -> concept.onError(connection, e));
        connection.setDisposable(disposable);
        return connection;
    }

    /**
     * 创建 Reactive Redis 的转发连接。
     * <p>
     * Create the connection to forward message by Reactive Redis.
     */
    @Override
    protected Connection createObservable(String id, String topic, Map<Object, Object> context, ConnectionLoadBalanceConcept concept) {
        ReactiveRedisTopicObservableConnection connection = new ReactiveRedisTopicObservableConnection();
        connection.setId(id);
        connection.setTopic(topic);
        connection.setReactiveRedisTemplate(reactiveRedisTemplate);
        return connection;
    }

    @Override
    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return MessageIdempotentVerifier.VERIFIED;
    }

    @Override
    protected ConnectionServer getSubscribeServer() {
        return new ReactiveRedisConnectionServer(reactiveRedisTemplate);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ReactiveRedisConnectionServer implements ConnectionServer {

        private final ReactiveRedisTemplate<?, Object> reactiveRedisTemplate;

        @Override
        public String getInstanceId() {
            return null;
        }

        @Override
        public String getServiceId() {
            return "reactive-redis";
        }

        @Override
        public String getHost() {
            return null;
        }

        @Override
        public int getPort() {
            return 0;
        }

        @Override
        public Map<String, String> getMetadata() {
            return null;
        }

        @Override
        public URI getUri() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }
    }
}
