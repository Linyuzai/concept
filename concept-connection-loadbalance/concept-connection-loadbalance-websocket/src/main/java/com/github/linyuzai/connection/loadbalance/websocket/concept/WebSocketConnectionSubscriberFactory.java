package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.subscribe.ProtocolConnectionSubscriberFactory;

/**
 * ws 连接订阅者工厂抽象类。
 * <p>
 * Abstract class of ws connection subscriber factory.
 */
public abstract class WebSocketConnectionSubscriberFactory<T extends WebSocketConnection>
        extends ProtocolConnectionSubscriberFactory<T> {

    public WebSocketConnectionSubscriberFactory() {
        addScopes(WebSocketScoped.NAME);
    }
}
