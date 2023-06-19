package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.websocket.Session;
import java.net.URI;
import java.util.function.Consumer;

/**
 * {@link JavaxWebSocketConnection} 的连接订阅者
 */
@Getter
@NoArgsConstructor
public class JavaxWebSocketConnectionSubscriber extends
        ContainerWebSocketConnectionSubscriber<JavaxWebSocketConnection> {

    private Class<?> clientClass = JavaxWebSocketSubscriberEndpoint.class;

    public JavaxWebSocketConnectionSubscriber(Class<?> clientClass) {
        this.clientClass = clientClass;
    }

    @SneakyThrows
    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept, Consumer<JavaxWebSocketConnection> consumer) {
        Session session = getContainer().connectToServer(clientClass, uri);
        consumer.accept(new JavaxWebSocketConnection(session, Connection.Type.SUBSCRIBER));
    }

    @Override
    public String getType() {
        return "javax";
    }
}
