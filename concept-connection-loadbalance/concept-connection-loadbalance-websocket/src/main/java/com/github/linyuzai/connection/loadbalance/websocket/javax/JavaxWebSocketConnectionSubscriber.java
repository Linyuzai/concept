package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.function.Consumer;

/**
 * {@link JavaxWebSocketConnection} 的连接订阅者
 */
@Getter
@NoArgsConstructor
public class JavaxWebSocketConnectionSubscriber extends WebSocketConnectionSubscriber<JavaxWebSocketConnection> {

    private Class<?> clientClass = JavaxWebSocketSubscriberEndpoint.class;

    public JavaxWebSocketConnectionSubscriber(String protocol) {
        super(protocol);
    }

    public JavaxWebSocketConnectionSubscriber(Class<?> clientClass) {
        this.clientClass = clientClass;
    }

    public JavaxWebSocketConnectionSubscriber(String protocol, Class<?> clientClass) {
        super(protocol);
        this.clientClass = clientClass;
    }

    @SneakyThrows
    @Override
    public void doSubscribe(URI uri, WebSocketLoadBalanceConcept concept, Consumer<JavaxWebSocketConnection> consumer) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Session session = container.connectToServer(clientClass, uri);
        consumer.accept(new JavaxWebSocketConnection(session, Connection.Type.SUBSCRIBER));
    }

    @Override
    public String getType() {
        return "javax";
    }
}
