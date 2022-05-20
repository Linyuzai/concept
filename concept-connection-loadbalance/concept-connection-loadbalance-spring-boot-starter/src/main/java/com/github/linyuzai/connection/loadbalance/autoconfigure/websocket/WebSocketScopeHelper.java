package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionScope;
import com.github.linyuzai.connection.loadbalance.autoconfigure.ScopeHelper;
import lombok.AllArgsConstructor;

import java.util.List;

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
