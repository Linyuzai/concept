package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionCloseEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEstablishEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

/**
 * 连接订阅日志
 */
public class ConnectionSubscribeLogger extends AbstractScoped implements ConnectionEventListener {

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof ConnectionEstablishEvent) {
            Connection connection = ((ConnectionEstablishEvent) event).getConnection();
            if (connection.isSubscriberType()) {
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                concept.getLogger().info("Subscribe on " + getServer(server));
            }
        } else if (event instanceof ConnectionSubscribeErrorEvent) {
            Throwable e = ((ConnectionSubscribeErrorEvent) event).getError();
            if (e instanceof ConnectionServerSubscribeException) {
                ConnectionServer server = ((ConnectionServerSubscribeException) e).getConnectionServer();
                concept.getLogger().error("Error subscribe " + getServer(server), e);
            }
        } else if (event instanceof ConnectionCloseEvent) {
            Connection connection = ((ConnectionCloseEvent) event).getConnection();
            Object reason = ((ConnectionCloseEvent) event).getReason();
            if (connection.isSubscriberType()) {
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                concept.getLogger().info("Unsubscribe on " + getServer(server) + getReason(reason));
            }
        }
    }

    public String getServer(ConnectionServer server) {
        if (server == null) {
            return "UnknownServer";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(server.getServiceId());
        if (server.getHost() != null) {
            builder.append(":").append(server.getHost());
        }
        if (server.getPort() > 0) {
            builder.append(":").append(server.getPort());
        }
        return builder.toString();
    }

    public String getReason(Object reason) {
        return reason == null ? "" : ", " + reason;
    }
}
