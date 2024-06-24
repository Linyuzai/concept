package com.github.linyuzai.connection.loadbalance.websocket.concept;

import org.springframework.http.HttpRequest;

import java.net.InetSocketAddress;

public interface WebSocketRequest extends HttpRequest {

    /**
     * Return the address on which the request was received.
     */
    InetSocketAddress getLocalAddress();

    /**
     * Return the address of the remote client.
     */
    InetSocketAddress getRemoteAddress();
}
