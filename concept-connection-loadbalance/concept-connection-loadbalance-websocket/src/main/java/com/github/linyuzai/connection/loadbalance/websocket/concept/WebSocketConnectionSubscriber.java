package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ServerConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.function.Consumer;

/**
 * ws 连接订阅者
 *
 * @param <T> 连接类
 */
@Getter
@Setter
public abstract class WebSocketConnectionSubscriber<T extends WebSocketConnection>
        extends ServerConnectionSubscriber<T> {

    private String protocol = "ws";

    @Override
    public void doSubscribe(ConnectionServer server, ConnectionLoadBalanceConcept concept,
                            Consumer<T> connectionConsumer, Consumer<Throwable> errorConsumer) {
        URI uri = getUri(server);
        doSubscribe(uri, concept, connection -> {
            connection.getMetadata().put(ConnectionServer.class, server);
            connectionConsumer.accept(connection);
        }, errorConsumer);
    }

    public abstract void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept,
                                     Consumer<T> connectionConsumer, Consumer<Throwable> errorConsumer);

    @Override
    public String getEndpoint() {
        return WebSocketLoadBalanceConcept.SUBSCRIBER_ENDPOINT;
    }

    public abstract String getType();

    @Override
    public MasterSlave getMasterSlave() {
        return MasterSlave.UNSUPPORTED;
    }
}
