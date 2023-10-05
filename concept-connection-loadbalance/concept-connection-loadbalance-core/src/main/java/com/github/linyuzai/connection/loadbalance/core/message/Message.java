package com.github.linyuzai.connection.loadbalance.core.message;

import java.util.Map;

/**
 * 消息。
 * <p>
 * Message for sending.
 */
public interface Message {

    /**
     * 标记该消息是否进行广播。
     * <p>
     * Key of message header for broadcast.
     */
    String BROADCAST = "_lb:broadcast";

    /**
     * 标记该消息是否需要转发。
     * <p>
     * Key of message header for forward.
     */
    String FORWARD = "_lb:forward";

    /**
     * 消息ID。
     * <p>
     * Key of message header for message id.
     */
    String ID = "_lb:id";

    /**
     * 标记该消息由哪个实例发送。
     * <p>
     * Key of message header for message from.
     */
    String FROM = "_lb:from";

    /**
     * 标记该消息为二进制数据，转发解码需要依赖此属性。
     * <p>
     * Key of message header for message type.
     */
    String BINARY = "_lb:binary";

    /**
     * 标记该消息转发需要缓存。
     * <p>
     * Key of message header for message pooled.
     */
    String POOLED = "_lb:pooled";

    /**
     * 获得消息头。
     * <p>
     * Get message headers.
     */
    Map<String, String> getHeaders();

    /**
     * 获得消息体。
     * <p>
     * Get message payload.
     */
    <T> T getPayload();

    /**
     * 是否需要广播。
     * <p>
     * If the message need broadcast.
     */
    default boolean needBroadcast() {
        String broadcast = getHeaders().getOrDefault(BROADCAST, Boolean.TRUE.toString());
        return Boolean.parseBoolean(broadcast);
    }

    /**
     * 设置消息是否需要广播。
     * <p>
     * Set the message need broadcast or not.
     */
    default void setBroadcast(boolean broadcast) {
        getHeaders().put(BROADCAST, Boolean.valueOf(broadcast).toString());
    }

    /**
     * 是否需要转发。
     * <p>
     * If the message need forward.
     */
    default boolean needForward() {
        String forward = getHeaders().getOrDefault(FORWARD, Boolean.TRUE.toString());
        return Boolean.parseBoolean(forward);
    }

    /**
     * 设置消息是否需要转发。
     * <p>
     * Set the message need forward or not.
     */
    default void setForward(boolean forward) {
        getHeaders().put(FORWARD, Boolean.valueOf(forward).toString());
    }

    /**
     * 获得消息ID。
     * <p>
     * Get message id.
     */
    default String getId() {
        return getHeaders().get(ID);
    }

    /**
     * 设置消息ID。
     * <p>
     * Set message id.
     */
    default void setId(String id) {
        getHeaders().put(ID, id);
    }

    /**
     * 获得消息来源。
     * <p>
     * Get message's from.
     */
    default String getFrom() {
        return getHeaders().get(FROM);
    }

    /**
     * 设置消息来源。
     * <p>
     * Set message's from.
     */
    default void setFrom(String from) {
        getHeaders().put(FROM, from);
    }
}
