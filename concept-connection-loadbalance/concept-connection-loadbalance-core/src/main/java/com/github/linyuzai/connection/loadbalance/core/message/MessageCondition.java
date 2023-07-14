package com.github.linyuzai.connection.loadbalance.core.message;

/**
 * 消息条件。
 * 给消息添加条件。
 * <p>
 * Condition of message.
 * Add some conditions to the message.
 */
public interface MessageCondition {

    void apply(Message message);
}
