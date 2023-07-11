package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;

/**
 * {@link JavaxWebSocketConnection} 的连接订阅者
 */
@Getter
@RequiredArgsConstructor
public class JavaxWebSocketConnectionSubscriber extends
        ContainerWebSocketConnectionSubscriber<JavaxWebSocketConnection> {

    private final Class<?> clientClass;

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept,
                            Consumer<JavaxWebSocketConnection> onSuccess,
                            Consumer<Throwable> onError,
                            Runnable onComplete) {
        try {
            Session session = getContainer().connectToServer(clientClass, uri);
            JavaxWebSocketConnection connection = new JavaxWebSocketConnection(session);
            connection.setType(Connection.Type.SUBSCRIBER);
            onSuccess.accept(connection);
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public String getType() {
        return "javax";
    }
}
