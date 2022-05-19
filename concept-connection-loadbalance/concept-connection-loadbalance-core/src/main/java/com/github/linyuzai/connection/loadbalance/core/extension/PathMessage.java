package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessage;

public class PathMessage extends ObjectMessage {

    public PathMessage(Object payload, String... paths) {
        super(payload);
        getHeaders().put(PathSelector.KEY, String.join(",", paths));
    }
}
