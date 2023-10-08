package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import org.springframework.util.ClassUtils;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

public class DefaultServletWebSocketClientFactory implements ServletWebSocketClientFactory {

    private static final boolean jettyPresent;

    static {
        ClassLoader loader = ServletWebSocketConnectionSubscriber.class.getClassLoader();
        jettyPresent = ClassUtils.isPresent("org.eclipse.jetty.websocket.client.WebSocketClient", loader);
    }

    @Override
    public WebSocketClient create() {
        if (jettyPresent) {
            return new JettyWebSocketClient();
        } else {
            return new StandardWebSocketClient();
        }
    }
}
