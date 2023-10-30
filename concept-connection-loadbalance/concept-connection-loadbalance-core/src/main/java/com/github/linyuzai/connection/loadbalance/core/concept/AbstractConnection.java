package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionCloseErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncodeException;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * 连接抽象类。
 * <p>
 * Abstract connection.
 */
@Getter
@Setter
public abstract class AbstractConnection implements Connection {

    protected String type;

    protected Map<Object, Object> metadata = new ConcurrentHashMap<>();

    protected MessageEncoder messageEncoder;

    protected MessageDecoder messageDecoder;

    protected MessageRetryStrategy messageRetryStrategy;

    protected final List<MessageSendInterceptor> messageSendInterceptors = new CopyOnWriteArrayList<>();

    protected final List<ConnectionCloseInterceptor> connectionCloseInterceptors = new CopyOnWriteArrayList<>();

    protected ConnectionLoadBalanceConcept concept;

    /**
     * 是否关闭。
     * <p>
     * If closed.
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected AtomicBoolean closed = new AtomicBoolean(false);

    /**
     * 是否还存活。
     * <p>
     * If alive.
     */
    protected volatile boolean alive = true;

    /**
     * 最后一次心跳时间。
     * <p>
     * The final heartbeat time.
     */
    protected volatile long lastHeartbeat = System.currentTimeMillis();

    public void addMetadata(Map<?, ?> metadata) {
        if (metadata == null) {
            return;
        }
        this.metadata.putAll(metadata);
    }

    /**
     * 发送消息。
     * 发送成功会发布 {@link MessageSendSuccessEvent}。
     * 发送失败会发布 {@link MessageSendErrorEvent}。
     * <p>
     * Send messages.
     * Publish {@link MessageSendSuccessEvent} if send successful.
     * Publish {@link MessageSendErrorEvent} if send unsuccessful.
     */
    @Override
    public void send(@NonNull Message message) {
        send(message, () -> concept.getEventPublisher()
                .publish(new MessageSendSuccessEvent(this, message)), e ->
                concept.getEventPublisher()
                        .publish(new MessageSendErrorEvent(this, message, e)), () -> {
        });
    }

    /**
     * 发送消息。
     * <p>
     * Send messages.
     */
    @Override
    public void send(@NonNull Message message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        //如果连接已经关闭则直接返回异常
        //Return error if connection has been closed
        if (isClosed()) {

            onError.accept(new IllegalStateException("Connection is closed"));
            onComplete.run();
            return;
        }
        if (message instanceof PingMessage) {
            //ping
            ping((PingMessage) message, onSuccess, onError, onComplete);
        } else if (message instanceof PongMessage) {
            //pong
            pong((PongMessage) message, onSuccess, onError, onComplete);
        } else {
            //遍历拦截器，消息被拦截则直接返回
            //Foreach interceptors and return if intercepted
            if (!messageSendInterceptors.isEmpty()) {
                try {
                    for (MessageSendInterceptor interceptor : messageSendInterceptors) {
                        if (interceptor.intercept(message, this)) {
                            return;
                        }
                    }
                } catch (Throwable e) {
                    onError.accept(e);
                    onComplete.run();
                    return;
                }
            }

            //编码消息
            //Encode messages
            Object encode;
            try {
                MessageEncoder encoder = getMessageEncoder();
                encode = encoder.encode(message, this);
            } catch (Throwable e) {
                onError.accept(new MessageEncodeException(message, e));
                onComplete.run();
                return;
            }

            //发送，失败重试
            //Send and retry if failure
            Consumer<Consumer<Throwable>> send = consumer ->
                    doSend(encode, onSuccess, consumer, onComplete);
            send.accept(e -> {
                if (e instanceof MessageTransportException) {
                    messageRetryStrategy.retry(e, send, onError);
                } else {
                    onError.accept(e);
                }
            });
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
        if (closed.get()) {
            return;
        }
        if (closed.compareAndSet(false, true)) {
            if (!connectionCloseInterceptors.isEmpty()) {
                try {
                    for (ConnectionCloseInterceptor interceptor : connectionCloseInterceptors) {
                        if (interceptor.intercept(reason, this)) {
                            return;
                        }
                    }
                } catch (Throwable e) {
                    onError.accept(e);
                    onComplete.run();
                    return;
                }
            }
            doClose(adaptCloseReason(reason), onSuccess, onError, () -> {
                concept.onClose(this, reason);
                onComplete.run();
            });
        }
    }

    protected Object adaptCloseReason(Object reason) {
        return reason;
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }

    /**
     * 发送数据。
     * <p>
     * Send data messages.
     */
    public abstract void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    /**
     * 发送 ping。
     * <p>
     * Send ping.
     */
    public abstract void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    /**
     * 发送 pong。
     * <p>
     * Send pong.
     */
    public abstract void doPong(PongMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    /**
     * 关闭。
     * <p>
     * Close.
     */
    public abstract void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);
}
