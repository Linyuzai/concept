package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.intercept.ConnectionRequest;
import org.springframework.http.HttpRequest;

import java.net.InetSocketAddress;

/**
 * WebSocket 连接请求。
 * <p>
 * WebSocket request.
 *
 * @since 2.7.0
 */
public interface WebSocketRequest extends ConnectionRequest, HttpRequest {

    /**
     * Return the address on which the request was received.
     */
    InetSocketAddress getLocalAddress();

    /**
     * Return the address of the remote client.
     */
    InetSocketAddress getRemoteAddress();
}
