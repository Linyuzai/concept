package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.extension.PathMessage;
import com.github.linyuzai.connection.loadbalance.core.extension.PathSelector;

/**
 * 默认的路径选择器
 * <p>
 * 配合 {@link PathMessage} 使用
 */
public class DefaultEndpointPathSelector extends PathSelector {

    public DefaultEndpointPathSelector() {
        super(WebSocketLoadBalanceConcept.SERVER_ENDPOINT_PREFIX);
    }

    @Override
    public String getPath(Connection connection) {
        return ((WebSocketConnection) connection).getUri().getPath();
    }
}
