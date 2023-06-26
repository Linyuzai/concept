package com.github.linyuzai.connection.loadbalance.core.message;

import java.util.Map;

/**
 * 消息
 */
public interface Message {

    /**
     * 标记该消息进行广播
     */
    String BROADCAST = "_broadcast";

    /**
     * 标记该消息已经经过转发
     */
    String FORWARD = "_forward";

    /**
     * 消息ID
     */
    String ID = "_id";

    /**
     * 标记该消息由哪个实例发送
     */
    String FROM = "_from";

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

    String getId();

    void setId(String id);

    String getFrom();

    void setFrom(String from);
}
