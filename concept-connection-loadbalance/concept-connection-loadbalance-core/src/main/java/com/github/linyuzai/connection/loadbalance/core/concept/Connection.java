package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendInterceptor;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.NonNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * 表示一个连接
 */
public interface Connection {

    /**
     * 返回连接 id
     *
     * @return 连接 id
     */
    Object getId();

    /**
     * 获得该连接的类型
     *
     * @return 连接类型
     * @see Type
     */
    String getType();

    default boolean isClientType() {
       return Type.isClient(getType());
    }

    default boolean isSubscriberType() {
        return Type.isSubscriber(getType());
    }

    default boolean isObservableType() {
        return Type.isObservable(getType());
    }

    /**
     * 获得连接的元数据
     *
     * @return 连接的元数据
     */
    Map<Object, Object> getMetadata();

    void setMessageRetryStrategy(MessageRetryStrategy strategy);

    MessageRetryStrategy getMessageRetryStrategy();

    /**
     * 获得消息发送拦截器列表
     *
     * @return 消息发送拦截器列表
     */
    List<MessageSendInterceptor> getMessageSendInterceptors();

    /**
     * 设置消息编码器
     *
     * @param encoder 消息编码器
     */
    void setMessageEncoder(MessageEncoder encoder);

    /**
     * 获得消息编码器
     *
     * @return 消息编码器
     */
    MessageEncoder getMessageEncoder();

    /**
     * 设置消息解码器
     *
     * @param decoder 消息解码器
     */
    void setMessageDecoder(MessageDecoder decoder);

    /**
     * 获得消息解码器
     *
     * @return 消息解码器
     */
    MessageDecoder getMessageDecoder();

    /**
     * 设置 {@link ConnectionLoadBalanceConcept}
     *
     * @param concept {@link ConnectionLoadBalanceConcept}
     */
    void setConcept(@NonNull ConnectionLoadBalanceConcept concept);

    /**
     * 获得 {@link ConnectionLoadBalanceConcept}
     *
     * @return {@link ConnectionLoadBalanceConcept}
     */
    ConnectionLoadBalanceConcept getConcept();

    /**
     * 发送消息
     *
     * @param message 消息
     */
    void send(@NonNull Message message);

    /**
     * 发送消息
     *
     * @param message 消息
     */
    void send(@NonNull Message message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    /**
     * 关闭连接
     */
    void close();

    /**
     * 关闭连接
     */
    void close(Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    /**
     * 关闭连接
     * <p>
     * 这里可以借鉴ws通过先发送一个约定的数据来关闭
     *
     * @param reason 原因
     */
    void close(Object reason);

    /**
     * 关闭连接
     * <p>
     * 这里可以借鉴ws通过先发送一个约定的数据来关闭
     *
     * @param reason 原因
     */
    void close(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    /**
     * 连接是否存活
     * <p>
     * 当开启心跳检测时方便关闭已断开的连接
     *
     * @return 连接是否存活
     */
    boolean isAlive();

    /**
     * 设置连接是否存活
     * <p>
     * 可以通过一些心跳机制来更新
     *
     * @param alive 连接是否存活
     */
    void setAlive(boolean alive);

    /**
     * 获得最后的心跳时间
     * <p>
     * 方便判断连接是否断开
     *
     * @return 最后的心跳时间
     */
    long getLastHeartbeat();

    /**
     * 设置最后的心跳时间
     *
     * @param lastHeartbeat 最后的心跳时间
     */
    void setLastHeartbeat(long lastHeartbeat);

    /**
     * 连接类型
     */
    interface Type {

        /**
         * 普通的客户端连接
         */
        String CLIENT = "Connection@client";

        /**
         * 用于监听其他服务的连接
         * <p>
         * 接收转发的消息
         */
        String SUBSCRIBER = "Connection@subscriber";

        /**
         * 用于被其他服务监听的连接
         * <p>
         * 转发消息
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
}
