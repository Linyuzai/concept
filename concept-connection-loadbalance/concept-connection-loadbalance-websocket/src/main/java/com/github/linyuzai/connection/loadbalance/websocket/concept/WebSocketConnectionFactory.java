package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;

/**
 * ws 连接工厂抽象类
 *
 * @param <T> 连接类
 */
public abstract class WebSocketConnectionFactory<T extends WebSocketConnection>
        extends AbstractConnectionFactory<T> {

    public WebSocketConnectionFactory() {
        addScopes(WebSocketScoped.NAME);
    }
}
