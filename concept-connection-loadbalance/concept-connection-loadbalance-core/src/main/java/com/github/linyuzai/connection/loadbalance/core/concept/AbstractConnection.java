package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionCloseErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncodeException;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * 连接抽象类
 */
@Getter
@Setter
public abstract class AbstractConnection implements Connection {

    protected final Map<Object, Object> metadata = Collections.synchronizedMap(new LinkedHashMap<>());

    protected final List<MessageSendInterceptor> messageSendInterceptors = new CopyOnWriteArrayList<>();

    @NonNull
    protected String type;

    @NonNull
    protected MessageRetryStrategy messageRetryStrategy;

    @NonNull
    protected MessageEncoder messageEncoder;

    @NonNull
    protected MessageDecoder messageDecoder;

    @NonNull
    protected ConnectionLoadBalanceConcept concept;

    protected volatile boolean closed;

    /**
     * 是否还存活
     */
    protected volatile boolean alive;

    /**
     * 最后一次心跳时间
     */
    protected volatile long lastHeartbeat;

    public AbstractConnection(@NonNull String type) {
        this(type, null);
    }

    public AbstractConnection(@NonNull String type, Map<Object, Object> metadata) {
        this.type = type;
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
        this.alive = true;
        this.lastHeartbeat = System.currentTimeMillis();
    }

    @Override
    public void send(@NonNull Message message) {
        send(message, () -> {
        }, e ->
                concept.getEventPublisher()
                        .publish(new MessageSendErrorEvent(this, message, e)), () -> {
        });
    }

    @Override
    public void send(@NonNull Message message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        if (isClosed()) {
            onError.accept(new IllegalStateException("Connection is closed"));
            onComplete.run();
            return;
        }
        if (message instanceof PingMessage) {
            ping((PingMessage) message, onSuccess, onError, onComplete);
        } else if (message instanceof PongMessage) {
            pong((PongMessage) message, onSuccess, onError, onComplete);
        } else {
            try {
                for (MessageSendInterceptor interceptor : messageSendInterceptors) {
                    if (!interceptor.intercept(message, this)) {
                        return;
                    }
                }
            } catch (Throwable e) {
                onError.accept(e);
                onComplete.run();
                return;
            }
            Object encode;
            try {
                MessageEncoder encoder = getMessageEncoder();
                encode = encoder.encode(message);
            } catch (Throwable e) {
                onError.accept(new MessageEncodeException(message, e));
                onComplete.run();
                return;
            }

            Consumer<Consumer<Throwable>> send = consumer ->
                    doSend(encode, onSuccess, consumer, onComplete);
            send.accept(e -> messageRetryStrategy.retry(e, send, onError));
        }
    }

    public void ping(PingMessage message) {
        ping(message, onMessageSuccess(message), onMessageError(message), onMessageComplete());
    }

    public void ping(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        doPing(message, onSuccess, onError, onComplete);
    }

    public void pong(PongMessage message) {
        pong(message, onMessageSuccess(message), onMessageError(message), onMessageComplete());
    }

    public void pong(PongMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        doPong(message, onSuccess, onError, onComplete);
    }

    protected Runnable onMessageSuccess(Message message) {
        return () -> concept.getEventPublisher()
                .publish(new MessageSendSuccessEvent(this, message));
    }

    protected Consumer<Throwable> onMessageError(Message message) {
        return e -> concept.getEventPublisher()
                .publish(new MessageSendErrorEvent(this, message, e));
    }

    protected Runnable onMessageComplete() {
        return () -> {
        };
    }

    @Override
    public void close() {
        close(null);
    }

    @Override
    public void close(Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        close(null, onSuccess, onError, onComplete);
    }

    @Override
    public void close(Object reason) {
        close(reason, () -> {
        }, e -> concept.getEventPublisher()
                .publish(new ConnectionCloseErrorEvent(this, reason, e)), () -> {
        });
    }

    @Override
    public void close(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        closed = true;
        doClose(reason, onSuccess, onError, onComplete);
        concept.onClose(this, reason);
    }

    public abstract void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    public abstract void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    public abstract void doPong(PongMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    public abstract void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);
}
