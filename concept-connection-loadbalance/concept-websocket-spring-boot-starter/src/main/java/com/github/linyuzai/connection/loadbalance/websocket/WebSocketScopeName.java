package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.scope.ScopeName;

/**
 * ws 连接域名称
 */
public class WebSocketScopeName extends ScopeName {

    public WebSocketScopeName() {
        super(WebSocketScope.NAME);
    }
}
