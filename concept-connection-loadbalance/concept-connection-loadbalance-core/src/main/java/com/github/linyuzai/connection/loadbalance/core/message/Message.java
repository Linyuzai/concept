package com.github.linyuzai.connection.loadbalance.core.message;

import java.util.Map;

/**
 * 消息
 */
public interface Message {

    /**
     * 标记该消息进行广播
     */
    String BROADCAST = "concept-connection-broadcast";

    /**
     * 标记该消息已经经过转发
     */
    String FORWARD = "concept-connection-forward";

    /**
     * 获得消息头
     *
     * @return 消息头
     */
    Map<String, String> getHeaders();

    /**
     * 获得消息体
     *
     * @param <T> 消息体类型
     * @return 消息体
     */
    <T> T getPayload();

    default boolean isType(Class<?> type) {
        if (type == null) {
            return false;
        }
        return type.isInstance(getPayload());
    }

    boolean isBroadcast();

    void setBroadcast(boolean broadcast);

    boolean isForward();

    void setForward(boolean forward);
}
