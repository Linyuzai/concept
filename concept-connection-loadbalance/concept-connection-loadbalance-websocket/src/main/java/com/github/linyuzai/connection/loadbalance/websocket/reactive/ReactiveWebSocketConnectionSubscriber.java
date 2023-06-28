package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceException;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.ClassUtils;
import org.springframework.web.reactive.socket.client.*;
import org.xnio.OptionMap;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import java.net.URI;
import java.util.function.Consumer;

/**
 * {@link ReactiveWebSocketConnection} 的连接订阅者
 */
@NoArgsConstructor
public class ReactiveWebSocketConnectionSubscriber extends
        WebSocketConnectionSubscriber<ReactiveWebSocketConnection> {

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

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept,
                            Consumer<ReactiveWebSocketConnection> connectionConsumer,
                            Consumer<Throwable> errorConsumer) {
        WebSocketClient client = newWebSocketClient();
        ReactiveWebSocketSubscriberHandler handler =
                new ReactiveWebSocketSubscriberHandler(concept, (session, sink) ->
                        connectionConsumer.accept(new ReactiveWebSocketConnection(session, sink,
                                Connection.Type.SUBSCRIBER)));
        client.execute(uri, handler).subscribe(unused -> {
        }, errorConsumer);
    }

    @SneakyThrows
    public WebSocketClient newWebSocketClient() {
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

    @Override
    public String getType() {
        return "reactive";
    }
}
