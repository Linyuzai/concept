package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;
import java.util.Collection;

@RequiredArgsConstructor
public abstract class ConnectionHeartbeatAutoSupport implements ConnectionEventListener {

    private ConnectionLoadBalanceConcept concept;

    private final Collection<String> connectionTypes;

    private final long timeout;

    @Override
    public void onEvent(Object event) {
        if (event instanceof ConnectionLoadBalanceConceptInitializeEvent) {
            concept = ((ConnectionLoadBalanceConceptInitializeEvent) event).getConcept();
            onInitialize();
        } else if (event instanceof ConnectionLoadBalanceConceptDestroyEvent) {
            onDestroy();
        } else if (event instanceof ConnectionEvent) {
            Connection connection = ((ConnectionEvent) event).getConnection();
            for (String connectionType : connectionTypes) {
                if (connection.getType().equals(connectionType)) {
                    if (event instanceof MessageReceiveEvent) {
                        Message message = ((MessageReceiveEvent) event).getMessage();
                        if (isPongMessage(message)) {
                            connection.setLastHeartbeat(System.currentTimeMillis());
                            connection.setAlive(true);
                        }
                    }
                }
            }
        }
    }

    public abstract void onInitialize();

    public abstract void onDestroy();

    public void sendPing() {
        for (String connectionType : connectionTypes) {
            Collection<Connection> connections = concept.getConnections(connectionType);
            Message message = createPingMessage();
            for (Connection connection : connections) {
                try {
                    connection.send(message);
                } catch (Throwable e) {
                    concept.publish(new HeartbeatSendErrorEvent(connection, e));
                }
            }
            concept.publish(new HeartbeatSendEvent(connections, connectionType));
        }
    }

    public void closeTimeout() {
        long now = System.currentTimeMillis();
        for (String connectionType : connectionTypes) {
            Collection<Connection> connections = concept.getConnections(connectionType);
            for (Connection connection : connections) {
                long lastHeartbeat = connection.getLastHeartbeat();
                if (now - lastHeartbeat > timeout) {
                    connection.setAlive(false);
                    connection.close("HeartbeatTimeout");
                }
            }
        }
    }

    public boolean isPongMessage(Message message) {
        return message instanceof PongMessage;
    }

    public Message createPingMessage() {
        return new BinaryPingMessage(ByteBuffer.allocate(0));
    }
}
