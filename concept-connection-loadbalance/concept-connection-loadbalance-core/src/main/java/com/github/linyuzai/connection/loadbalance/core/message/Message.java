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
    boolean needBroadcast();

    /**
     * 设置消息是否需要广播。
     * <p>
     * Set the message need broadcast or not.
     */
    void setBroadcast(boolean broadcast);

    /**
     * 是否需要转发。
     * <p>
     * If the message need forward.
     */
    boolean needForward();

    /**
     * 设置消息是否需要转发。
     * <p>
     * Set the message need forward or not.
     */
    void setForward(boolean forward);

    /**
     * 获得消息ID。
     * <p>
     * Get message id.
     */
    String getId();

    /**
     * 设置消息ID。
     * <p>
     * Set message id.
     */
    void setId(String id);

    /**
     * 获得消息来源。
     * <p>
     * Get message's from.
     */
    String getFrom();

    /**
     * 设置消息来源。
     * <p>
     * Set message's from.
     */
    void setFrom(String from);
}
