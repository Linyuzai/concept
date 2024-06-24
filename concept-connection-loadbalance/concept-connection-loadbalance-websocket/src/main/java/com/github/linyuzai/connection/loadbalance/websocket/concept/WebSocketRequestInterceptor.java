package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.intercept.ConnectionRequestInterceptor;

/**
 * WebSocket 握手拦截器。
 * <p>
 * Interceptor for Handshaking.
 *
 * @since 2.7.0
 */
public interface WebSocketRequestInterceptor extends ConnectionRequestInterceptor<WebSocketRequest, WebSocketResponse>, WebSocketScoped {

}
