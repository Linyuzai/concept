package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendInterceptor;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 表示一个连接。
 * <p>
 * A connection.
 */
public interface Connection {

    /**
     * 获得连接 ID。
     * <p>
     * Get id.
     */
    Object getId();

    /**
     * 获得该连接的类型。
     * <p>
     * Get type.
     *
     * @see Type
     */
    String getType();

    /**
     * 是否是客户端类型。
     * <p>
     * If client type.
     */
    default boolean isClientType() {
        return Type.isClient(getType());
    }

    /**
     * 是否是订阅者类型。
     * <p>
     * If subscriber type.
     */
    default boolean isSubscriberType() {
        return Type.isSubscriber(getType());
    }

    /**
     * 是否是被观察的类型。
     * <p>
     * If observable type.
     */
    default boolean isObservableType() {
        return Type.isObservable(getType());
    }

    /**
     * 获得连接的元数据。
     * <p>
     * Get metadata.
     */
    Map<Object, Object> getMetadata();

    /**
     * 设置消息编码器。
     * <p>
     * Set encoder for message.
     */
    void setMessageEncoder(MessageEncoder encoder);

    /**
     * 获得消息编码器。
     * <p>
     * Get encoder for message.
     */
    MessageEncoder getMessageEncoder();

    /**
     * 设置消息解码器。
     * <p>
     * Set decoder for message.
     */
    void setMessageDecoder(MessageDecoder decoder);

    /**
     * 获得消息解码器。
     * <p>
     * Get decoder for message.
     */
    MessageDecoder getMessageDecoder();

    /**
     * 设置消息重试策略。
     * <p>
     * Set retry strategy for message sending.
     */
    void setMessageRetryStrategy(MessageRetryStrategy strategy);

    /**
     * 获得消息重试策略。
     * <p>
     * Get retry strategy for message sending.
     */
    MessageRetryStrategy getMessageRetryStrategy();

    /**
     * 获得消息发送拦截器。
     * <p>
     * Get interceptors for message sending.
     */
    List<MessageSendInterceptor> getMessageSendInterceptors();

    /**
     * 获得连接关闭拦截器。
     * <p>
     * Get interceptors for connection closing.
     */
    List<ConnectionCloseInterceptor> getConnectionCloseInterceptors();

    /**
     * 设置 Concept。
     * <p>
     * Set Concept.
     */
    void setConcept(@NonNull ConnectionLoadBalanceConcept concept);

    /**
     * 获得 Concept。
     * <p>
     * Get Concept.
     */
    ConnectionLoadBalanceConcept getConcept();

    /**
     * 初始化。
     * <p>
     * initialize.
     */
    default void initialize() {

    }

    /**
     * 发送消息。
     * <p>
     * Send message.
     */
    void send(@NonNull Message message);

    /**
     * 发送消息。
     * 结果通过回调返回。
     * <p>
     * Send message.
     * Result returned by callback.
     */
    void send(@NonNull Message message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    /**
     * 关闭连接。
     * <p>
     * Close.
     */
    void close();

    /**
     * 关闭连接。
     * 结果通过回调返回。
     * <p>
     * Close.
     * Result returned by callback.
     */
    void close(Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    /**
     * 关闭连接。
     * 指定关闭的原因。
     * <p>
     * Close with reason.
     */
    void close(Object reason);

    /**
     * 关闭连接。
     * 指定关闭的原因。
     * 结果通过回调返回。
     * <p>
     * Close with reason.
     * Result returned by callback.
     */
    void close(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    boolean isClosed();

    /**
     * 连接是否存活。
     * <p>
     * If alive.
     */
    boolean isAlive();

    /**
     * 设置连接是否存活。
     * <p>
     * Set state when alive or not alive.
     */
    void setAlive(boolean alive);

    /**
     * 获得最后的心跳时间。
     * <p>
     * Get the final heartbeat time.
     */
    long getLastHeartbeat();

    /**
     * 设置最后的心跳时间。
     * <p>
     * Set the final heartbeat time.
     */
    void setLastHeartbeat(long lastHeartbeat);

    /**
     * 连接类型。
     * <p>
     * Connection's type.
     */
    interface Type {

        /**
         * 普通的客户端连接。
         * <p>
         * The normal client connection.
         */
        String CLIENT = "Connection@client";

        /**
         * 监听其他服务的连接。
         * 接收转发的消息。
         * <p>
         * The subscriber to listen other connections.
         * Messages received from this type of connection will be forwarded.
         */
        String SUBSCRIBER = "Connection@subscriber";

        /**
         * 被其他服务监听的连接。
         * 转发消息。
         * <p>
         * The observable to forward messages.
         */
        String OBSERVABLE = "Connection@observable";

        static boolean isClient(String type) {
            return CLIENT.equals(type);
        }

        static boolean isSubscriber(String type) {
            return SUBSCRIBER.equals(type);
        }

        static boolean isObservable(String type) {
            return OBSERVABLE.equals(type);
        }
    }

    interface Close {

        int CODE = 4096;

        String HEARTBEAT_TIMEOUT = "HeartbeatTimeout";

        String NOT_ALIVE = "NotAlive";

        String SERVER_STOP = "ServerStop";
    }
}
