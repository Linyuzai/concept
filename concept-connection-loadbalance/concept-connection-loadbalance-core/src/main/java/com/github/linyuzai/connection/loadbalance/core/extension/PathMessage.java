package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessage;

import java.util.Arrays;
import java.util.Collection;

/**
 * 可以将消息发送给指定路径的连接
 * <p>
 * 配合 {@link PathSelector} 使用
 */
public class PathMessage extends ObjectMessage {

    public PathMessage(Object payload, String... paths) {
        this(payload, Arrays.asList(paths));
    }

    public PathMessage(Object payload, Collection<String> paths) {
        super(payload);
        getHeaders().put(PathSelector.KEY, String.join(",", paths));
    }
}
