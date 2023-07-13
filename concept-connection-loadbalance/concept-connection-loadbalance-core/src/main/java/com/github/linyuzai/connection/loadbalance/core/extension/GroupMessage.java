package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCondition;
import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessage;

import java.util.Arrays;
import java.util.Collection;

/**
 * 基于分组的消息。
 * 可以给某几个分组发送消息。
 * 配合 {@link GroupSelector} 使用。
 * <p>
 * Messages based on groups.
 * Message will be send to groups with {@link GroupSelector}.
 */
public class GroupMessage extends ObjectMessage implements MessageCondition {

    public GroupMessage(Object payload, String... groups) {
        this(payload, Arrays.asList(groups));
    }

    public GroupMessage(Object payload, Collection<String> groups) {
        super(payload);
        getHeaders().put(GroupSelector.KEY, String.join(",", groups));
    }

    @Override
    public void apply(Message message) {
        message.getHeaders().put(GroupSelector.KEY, getHeaders().get(GroupSelector.KEY));
    }

    public static MessageCondition condition(String... groups) {
        return new GroupMessage(null, groups);
    }

    public static MessageCondition condition(Collection<String> groups) {
        return new GroupMessage(null, groups);
    }
}
