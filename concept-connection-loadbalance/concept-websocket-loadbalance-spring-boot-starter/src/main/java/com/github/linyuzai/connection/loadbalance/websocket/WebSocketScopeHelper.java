package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.scope.ConnectionScope;
import com.github.linyuzai.connection.loadbalance.autoconfigure.scope.ScopeHelper;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * ws 连接域帮助器
 * <p>
 * 通过 {@link WebSocketScope} {@link ConnectionScope} 的顺序获得对象
 */
@AllArgsConstructor
public class WebSocketScopeHelper {

    private ScopeHelper helper;

    public <T> T getBean(Class<T> clazz) {
        return helper.getBean(clazz, WebSocketScope.class, ConnectionScope.class);
    }

    public <T> List<T> getBeans(Class<T> clazz) {
        return helper.getBeans(clazz, WebSocketScope.class, ConnectionScope.class);
    }
}
