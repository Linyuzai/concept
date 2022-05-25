package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.message.MessageReceiveEvent;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLoadBalanceLogger;

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
    public void onEvent(Object event) {
        if (event instanceof ConnectionEstablishEvent) {
            Connection connection = ((ConnectionEstablishEvent) event).getConnection();
            if (Connection.Type.SUBSCRIBER.equals(connection.getType())) {
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                info("Subscribe on " + server.getInstanceId());
            } else if (Connection.Type.OBSERVABLE.equals(connection.getType())) {

            }
        } else if (event instanceof ConnectionSubscribeErrorEvent) {
            ConnectionServer server = ((ConnectionSubscribeErrorEvent) event).getConnectionServer();
            Throwable e = ((ConnectionSubscribeErrorEvent) event).getError();
            error("Error subscribe " + server.getInstanceId(), e);
        } else if (event instanceof MessageReceiveEvent) {
            Connection connection = ((MessageReceiveEvent) event).getConnection();
            if (Connection.Type.OBSERVABLE.equals(connection.getType())) {

            }
        } else if (event instanceof ConnectionCloseEvent) {
            Connection connection = ((ConnectionCloseEvent) event).getConnection();
            Object reason = ((ConnectionCloseEvent) event).getReason();
            if (Connection.Type.SUBSCRIBER.equals(connection.getType())) {
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                info("Unsubscribe on " + server.getInstanceId() + getReason(reason));
            }
        } else if (event instanceof ConnectionErrorEvent) {
            Connection connection = ((ConnectionErrorEvent) event).getConnection();
            Throwable e = ((ConnectionErrorEvent) event).getError();
            if (Connection.Type.SUBSCRIBER.equals(connection.getType())) {
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                error("Error subscribe " + server.getInstanceId(), e);
            } else if (Connection.Type.OBSERVABLE.equals(connection.getType())) {
                error("Error observed", e);
            }
        }
    }

    public String getReason(Object reason) {
        return reason == null ? "" : ", " + reason;
    }
}
