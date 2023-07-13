package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.idempotent.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.StatusListener;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Reactive Redisson (Shared) Topic 连接订阅器。
 * 通过 {@link RTopicReactive} 转发消息，通过 {@link RTopicReactive} 订阅消息
 * <p>
 * {@link ConnectionSubscriber} impl by Reactive Redisson (Shared) Topic.
 * forward message by {@link RTopicReactive}, listen message by {@link RTopicReactive}.
 */
@Getter
@RequiredArgsConstructor
public class ReactiveRedissonTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final RedissonReactiveClient client;

    private final boolean shared;

    /**
     * 创建 Reactive Redisson 的监听连接。
     * <p>
     * Create the connection to listen message from Reactive Redisson.
     */
    @Override
    protected Connection createSubscriber(String id, String topic, Map<Object, Object> context,
                                          ConnectionLoadBalanceConcept concept) {
        RTopicReactive rTopic;
        if (shared) {
            rTopic = new RTopicReactiveWrapper(client.getShardedTopic(topic));
        } else {
            rTopic = client.getTopic(topic);
        }
        context.put(RTopicReactive.class, rTopic);
        ReactiveRedissonTopicSubscriberConnection connection = new ReactiveRedissonTopicSubscriberConnection();
        connection.setId(id);
        Disposable disposable = rTopic.getMessages(Object.class)
                .subscribe(object -> onMessageReceived(connection, object));
        connection.setDisposable(disposable);
        return connection;
    }

    /**
     * 创建 Reactive Redisson 的转发连接。
     * <p>
     * Create the connection to forward message by Reactive Redisson.
     */
    @Override
    protected Connection createObservable(String id, String topic, Map<Object, Object> context,
                                          ConnectionLoadBalanceConcept concept) {
        RTopicReactive rTopic = (RTopicReactive) context.get(RTopicReactive.class);
        ReactiveRedissonTopicObservableConnection connection = new ReactiveRedissonTopicObservableConnection();
        connection.setId(id);
        connection.setClient(client);
        connection.setTopic(rTopic);
        return connection;
    }

    @Override
    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return MessageIdempotentVerifier.VERIFIED;
    }

    @Override
    protected ConnectionServer getSubscribeServer() {
        return new RedissonConnectionServer(client);
    }

    @Getter
    @RequiredArgsConstructor
    public static class RedissonConnectionServer implements ConnectionServer {

        private final RedissonReactiveClient client;

        @Override
        public String getInstanceId() {
            return null;
        }

        @Override
        public String getServiceId() {
            return "redisson.reactive";
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

    @Getter
    @RequiredArgsConstructor
    public static class RTopicReactiveWrapper implements RTopicReactive {

        private final RShardedTopicReactive topic;

        @Override
        public List<String> getChannelNames() {
            return topic.getChannelNames();
        }

        @Override
        public Mono<Long> publish(Object message) {
            return topic.publish(message);
        }

        @Override
        public Mono<Integer> addListener(StatusListener listener) {
            return topic.addListener(listener);
        }

        @Override
        public <M> Mono<Integer> addListener(Class<M> type, MessageListener<M> listener) {
            return topic.addListener(type, listener);
        }

        @Override
        public Mono<Void> removeListener(Integer... listenerIds) {
            return topic.removeListener(listenerIds);
        }

        @Override
        public Mono<Void> removeListener(MessageListener<?> listener) {
            return topic.removeListener(listener);
        }

        @Override
        public <M> Flux<M> getMessages(Class<M> type) {
            return topic.getMessages(type);
        }

        @Override
        public Mono<Long> countSubscribers() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Mono<Void> removeAllListeners() {
            return topic.removeAllListeners();
        }
    }
}
