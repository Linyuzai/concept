package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCondition;
import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessage;

import java.util.Arrays;
import java.util.Collection;

/**
 * 基于路径的消息。
 * 可以将消息发送给指定路径的连接。
 * 配合 {@link PathSelector} 使用。
 * <p>
 * Messages based on paths.
 * Message will be sent to paths with {@link PathSelector}.
 */
public class PathMessage extends ObjectMessage implements MessageCondition {

    public PathMessage(Object payload, String... paths) {
        this(payload, Arrays.asList(paths));
    }

    public PathMessage(Object payload, Collection<String> paths) {
        super(payload);
        getHeaders().put(PathSelector.KEY, String.join(",", paths));
    }

    @Override
    public void apply(Message message) {
        message.getHeaders().put(PathSelector.KEY, getHeaders().get(PathSelector.KEY));
    }

    public static MessageCondition condition(String... paths) {
        return new PathMessage(null, paths);
    }

    public static MessageCondition condition(Collection<String> paths) {
        return new PathMessage(null, paths);
    }
}
