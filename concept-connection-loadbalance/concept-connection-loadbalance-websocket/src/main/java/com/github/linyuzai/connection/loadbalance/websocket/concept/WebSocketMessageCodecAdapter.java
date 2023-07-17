package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;

/**
 * ws 消息编解码适配器。
 * <p>
 * WebSocket message codec adapter.
 */
public abstract class WebSocketMessageCodecAdapter extends AbstractMessageCodecAdapter {

    public WebSocketMessageCodecAdapter() {
        addScopes(WebSocketScoped.NAME);
    }
}
