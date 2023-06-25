package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.extension.PathMessage;
import com.github.linyuzai.connection.loadbalance.core.extension.PathSelector;
import com.github.linyuzai.connection.loadbalance.core.select.FilterConnectionSelector;

/**
 * 默认的路径选择器
 * <p>
 * 配合 {@link PathMessage} 使用
 */
public class DefaultEndpointPathSelector extends PathSelector
        implements FilterConnectionSelector, WebSocketScoped {

    public DefaultEndpointPathSelector(String prefix) {
        super(prefix);
    }

    @Override
    public String getPath(Connection connection) {
        if (connection instanceof WebSocketConnection) {
            return ((WebSocketConnection) connection).getUri().getPath();
        }
        return null;
    }

    @Override
    public boolean support(String scope) {
        return WebSocketScoped.super.support(scope);
    }
}
