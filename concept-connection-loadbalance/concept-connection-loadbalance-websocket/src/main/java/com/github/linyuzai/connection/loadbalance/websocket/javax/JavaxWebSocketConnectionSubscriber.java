package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

@Getter
@NoArgsConstructor
public class JavaxWebSocketConnectionSubscriber extends WebSocketConnectionSubscriber {

    public JavaxWebSocketConnectionSubscriber(String protocol) {
        super(protocol);
    }

    @SneakyThrows
    @Override
    public Connection doSubscribe(ConnectionServer server, WebSocketLoadBalanceConcept concept) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = getUri(server);
        Session session = container.connectToServer(JavaxWebSocketClientEndpoint.class, uri);
        Connection connection = concept.getConnection(session.getId(), Connection.Type.SUBSCRIBER);
        if (connection != null) {
            connection.getMetadata().put(ConnectionServer.class, server);
        }
        return connection;
    }

    @Override
    public String getType() {
        return "standard";
    }
}
