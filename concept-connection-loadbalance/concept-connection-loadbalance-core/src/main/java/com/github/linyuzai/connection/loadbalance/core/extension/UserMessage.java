package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessage;

import java.util.Arrays;
import java.util.Collection;

/**
 * 基于用户的消息
 * <p>
 * 可以给某个用于发送消息
 * <p>
 * 配合 {@link UserIdSelector} 使用
 */
public class UserMessage extends ObjectMessage {

    public UserMessage(Object payload, String... userIds) {
        this(payload, Arrays.asList(userIds));
    }

    public UserMessage(Object payload, Collection<String> userIds) {
        super(payload);
        setBroadcast(false);
        getHeaders().put(UserIdSelector.KEY, String.join(",", userIds));
    }
}
