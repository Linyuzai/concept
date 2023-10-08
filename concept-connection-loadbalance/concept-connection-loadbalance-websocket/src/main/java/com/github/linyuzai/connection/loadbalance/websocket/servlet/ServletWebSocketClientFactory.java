package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import org.springframework.web.socket.client.WebSocketClient;

public interface ServletWebSocketClientFactory {

    WebSocketClient create();
}
