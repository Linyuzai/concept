package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

import static com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer.LB_HOST_PORT;

@Getter
@Setter
public abstract class SseConnection extends AbstractConnection {

    private SseCreation creation;

    @Override
    public Object getId() {
        return creation.getId();
    }

    public String getPath() {
        return creation.getPath();
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    @Override
    public void doPong(PongMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    public void closeObservable() {
        if (isObservableType()) {
            String lbHostPost = (String) getMetadata().get(LB_HOST_PORT);
            if (lbHostPost == null) {
                return;
            }
            concept.onClose(this, Close.NOT_ALIVE);
            Collection<Connection> connections = concept.getConnectionRepository()
                    .select(Type.SUBSCRIBER);
            for (Connection connection : connections) {
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                if (server == null) {
                    continue;
                }
                String url = ConnectionServer.url(server);
                if (Objects.equals(url, lbHostPost)) {
                    concept.onClose(connection, Close.NOT_ALIVE);
                    break;
                }
            }
        }
    }

    public void closeSubscriber() {
        if (isSubscriberType()) {
            ConnectionServer server = (ConnectionServer) getMetadata().get(ConnectionServer.class);
            if (server == null) {
                return;
            }
            concept.onClose(this, Close.NOT_ALIVE);
            Collection<Connection> connections = concept.getConnectionRepository()
                    .select(Type.OBSERVABLE);
            for (Connection connection : connections) {
                String lbHostPost = (String) getMetadata().get(LB_HOST_PORT);
                if (lbHostPost == null) {
                    continue;
                }
                String url = ConnectionServer.url(server);
                if (Objects.equals(url, lbHostPost)) {
                    concept.onClose(connection, Close.NOT_ALIVE);
                    break;
                }
            }
        }
    }
}
