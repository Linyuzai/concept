package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionCloseEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEstablishEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLoadBalanceLogger;
import com.github.linyuzai.connection.loadbalance.core.message.MessageReceiveEvent;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 连接订阅日志
 */
public class ConnectionSubscribeLogger extends ConnectionLoadBalanceLogger implements ConnectionEventListener {

    public ConnectionSubscribeLogger(Consumer<String> info, BiConsumer<String, Throwable> error) {
        super(info, error);
    }

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof ConnectionEstablishEvent) {
            Connection connection = ((ConnectionEstablishEvent) event).getConnection();
            if (Connection.Type.SUBSCRIBER.equals(connection.getType())) {
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                info("Subscribe on " + getServer(server));
            } else if (Connection.Type.OBSERVABLE.equals(connection.getType())) {

            }
        } else if (event instanceof ConnectionSubscribeErrorEvent) {
            ConnectionServer server = ((ConnectionSubscribeErrorEvent) event).getConnectionServer();
            Throwable e = ((ConnectionSubscribeErrorEvent) event).getError();
            error("Error subscribe " + getServer(server), e);
        } else if (event instanceof MessageReceiveEvent) {
            Connection connection = ((MessageReceiveEvent) event).getConnection();
            if (Connection.Type.OBSERVABLE.equals(connection.getType())) {

            }
        } else if (event instanceof ConnectionCloseEvent) {
            Connection connection = ((ConnectionCloseEvent) event).getConnection();
            Object reason = ((ConnectionCloseEvent) event).getReason();
            if (Connection.Type.SUBSCRIBER.equals(connection.getType())) {
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                info("Unsubscribe on " + getServer(server) + getReason(reason));
            }
        } else if (event instanceof ConnectionErrorEvent) {
            Connection connection = ((ConnectionErrorEvent) event).getConnection();
            Throwable e = ((ConnectionErrorEvent) event).getError();
            if (Connection.Type.SUBSCRIBER.equals(connection.getType())) {
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                error("Error subscribe " + getServer(server), e);
            } else if (Connection.Type.OBSERVABLE.equals(connection.getType())) {
                error("Error observed", e);
            }
        }
    }

    public String getReason(Object reason) {
        return reason == null ? "" : ", " + reason;
    }
}
