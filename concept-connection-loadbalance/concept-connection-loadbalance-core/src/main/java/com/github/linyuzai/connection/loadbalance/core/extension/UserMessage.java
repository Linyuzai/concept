package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCondition;
import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessage;

import java.util.Arrays;
import java.util.Collection;

/**
 * 基于用户的消息。
 * 可以给某个用户发送消息。
 * 配合 {@link UserSelector} 使用。
 * <p>
 * Messages based on users.
 * Message will be sent to users with {@link UserSelector}.
 */
public class UserMessage extends ObjectMessage implements MessageCondition {

    public UserMessage(Object payload, String... userIds) {
        this(payload, Arrays.asList(userIds));
    }

    public UserMessage(Object payload, Collection<String> userIds) {
        super(payload);
        //setBroadcast(false); 默认可以多端连接
        getHeaders().put(UserSelector.KEY, String.join(",", userIds));
    }

    @Override
    public void apply(Message message) {
        message.getHeaders().put(UserSelector.KEY, getHeaders().get(UserSelector.KEY));
    }

    public static MessageCondition condition(String... userIds) {
        return new UserMessage(null, userIds);
    }

    public static MessageCondition condition(Collection<String> userIds) {
        return new UserMessage(null, userIds);
    }
}
