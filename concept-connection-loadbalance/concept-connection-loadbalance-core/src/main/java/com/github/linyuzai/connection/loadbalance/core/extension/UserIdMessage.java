package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessage;

/**
 * 基于用户的消息
 * <p>
 * 可以给某个用于发送消息
 * <p>
 * 配合 {@link UserIdSelector} 使用
 */
public class UserIdMessage extends ObjectMessage {

    public UserIdMessage(Object payload, String... userIds) {
        super(payload);
        getHeaders().put(Message.BROADCAST, Boolean.FALSE.toString());
        getHeaders().put(UserIdSelector.KEY, String.join(",", userIds));
    }
}
