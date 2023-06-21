package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapterFactory;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

public abstract class WebSocketMessageCodecAdapterFactory extends AbstractScoped
        implements MessageCodecAdapterFactory {

    public WebSocketMessageCodecAdapterFactory() {
        addScopes(WebSocketScoped.NAME);
    }
}
