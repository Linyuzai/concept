package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutor;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLogger;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.idempotent.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapter;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 连接负载均衡概念。
 * <p>
 * Concept of connection's load-balance.
 */
public interface ConnectionLoadBalanceConcept {

    void initialize();

    void destroy();

    String getId();

    /**
     * 创建连接。
     * <p>
     * Create connection.
     */
    Connection createConnection(Object o, Map<Object, Object> metadata);

    /**
     * 当连接建立时调用。
     * <p>
     * Called when connection established.
     */
    Connection onEstablish(Object o, Map<Object, Object> metadata);

    /**
     * 当连接建立时调用。
     * <p>
     * Called when connection established.
     */
    void onEstablish(Connection connection);

    /**
     * 当连接关闭时调用。
     * <p>
     * Called when connection closed.
     */
    void onClose(Object id, String type, Object reason);

    /**
     * 当连接关闭时调用。
     * <p>
     * Called when connection closed for additional reason.
     */
    void onClose(Connection connection, Object reason);

    /**
     * 当连接接收消息时调用。
     * <p>
     * Called when message received.
     */
    void onMessage(Object id, String type, Object message);

    /**
     * 当连接接收消息时调用。
     * <p>
     * Called when message received.
     */
    void onMessage(Connection connection, Object message);

    /**
     * 当连接接收消息时调用。
     * 可通过断言确定是否丢弃消息。
     * <p>
     * Called when message received.
     * Discard messages by predicate.
     */
    void onMessage(Connection connection, Object message, Predicate<Message> predicate);

    /**
     * 当连接异常时调用。
     * <p>
     * Called when has error.
     */
    void onError(Object id, String type, Throwable e);

    /**
     * 当连接异常时调用。
     * <p>
     * Called when has error.
     */
    void onError(Connection connection, Throwable e);

    /**
     * 创建消息。
     * <p>
     * Create message.
     */
    Message createMessage(Object o);

    /**
     * 创建消息。
     * 同时添加额外的消息头。
     * <p>
     * Create message with additional headers.
     */
    Message createMessage(Object o, Map<String, String> headers);

    /**
     * 发送消息。
     * <p>
     * Send message.
     */
    void send(Object msg);

    /**
     * 发送消息。
     * 同时添加额外的消息头。
     * <p>
     * Send message with additional headers.
     */
    void send(Object msg, Map<String, String> headers);

    /**
     * 发送消息。
     * 同时指定连接选择器。
     * <p>
     * Send message with connection selector.
     */
    void send(Object msg, ConnectionSelector selector);

    /**
     * 发送消息。
     * 同时添加额外的消息头。
     * 同时指定连接选择器。
     * <p>
     * Send message with additional headers and connection selector.
     */
    void send(Object msg, Map<String, String> headers, ConnectionSelector selector);

    ConnectionRepository getConnectionRepository();

    ConnectionServerManager getConnectionServerManager();

    ConnectionSubscriber getConnectionSubscriber();

    List<ConnectionFactory> getConnectionFactories();

    List<ConnectionSelector> getConnectionSelectors();

    List<MessageFactory> getMessageFactories();

    MessageCodecAdapter getMessageCodecAdapter();

    MessageRetryStrategyAdapter getMessageRetryStrategyAdapter();

    MessageIdempotentVerifier getMessageIdempotentVerifier();

    ScheduledExecutor getScheduledExecutor();

    ConnectionLogger getLogger();

    ConnectionEventPublisher getEventPublisher();
}
