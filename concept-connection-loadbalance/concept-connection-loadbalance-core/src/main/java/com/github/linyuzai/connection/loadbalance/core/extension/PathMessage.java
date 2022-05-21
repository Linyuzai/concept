package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessage;

/**
 * 可以将消息发送给指定路径的连接
 * <p>
 * 配合 {@link PathSelector} 使用
 */
public class PathMessage extends ObjectMessage {

    public PathMessage(Object payload, String... paths) {
        super(payload);
        getHeaders().put(PathSelector.KEY, String.join(",", paths));
    }
}
