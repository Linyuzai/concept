package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import org.springframework.web.reactive.socket.client.WebSocketClient;

public interface ReactiveWebSocketClientFactory {

    WebSocketClient create();
}
