package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceException;
import lombok.SneakyThrows;
import org.springframework.util.ClassUtils;
import org.springframework.web.reactive.socket.client.*;
import org.xnio.OptionMap;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

public class DefaultReactiveWebSocketClientFactory implements ReactiveWebSocketClientFactory {

    private static final boolean tomcatPresent;

    private static final boolean jettyPresent;

    private static final boolean undertowPresent;

    private static final boolean reactorNettyPresent;

    static {
        ClassLoader loader = ReactiveWebSocketConnectionSubscriber.class.getClassLoader();
        tomcatPresent = ClassUtils.isPresent("org.apache.tomcat.websocket.WsWebSocketContainer", loader);
        jettyPresent = ClassUtils.isPresent("org.eclipse.jetty.websocket.client.WebSocketClient", loader);
        undertowPresent = ClassUtils.isPresent("io.undertow.websockets.core.WebSocketChannel", loader);
        reactorNettyPresent = ClassUtils.isPresent("reactor.netty.http.websocket.WebsocketInbound", loader);
    }

    @SneakyThrows
    @Override
    public WebSocketClient create() {
        if (reactorNettyPresent) {
            return new ReactorNettyWebSocketClient();
        } else if (undertowPresent) {
            XnioWorker worker = Xnio.getInstance().createWorker(OptionMap.builder().getMap());
            return new UndertowWebSocketClient(worker);
        } else if (jettyPresent) {
            return new JettyWebSocketClient();
        } else if (tomcatPresent) {
            return new TomcatWebSocketClient();
        } else {
            throw new WebSocketLoadBalanceException("No suitable client found");
        }
    }
}
