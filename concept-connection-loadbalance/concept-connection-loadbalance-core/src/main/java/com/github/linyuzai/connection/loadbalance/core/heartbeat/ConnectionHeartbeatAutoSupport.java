package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public abstract class ConnectionHeartbeatAutoSupport implements ConnectionEventListener {

    private final Map<Object, Long> last = new ConcurrentHashMap<>();

    private ConnectionLoadBalanceConcept concept;

    private final String connectionType;

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
            if (connection.getType().equals(connectionType)) {
                if (event instanceof ConnectionOpenEvent) {
                    last.put(connection.getId(), System.currentTimeMillis());
                } else if (event instanceof ConnectionCloseEvent || event instanceof ConnectionCloseErrorEvent) {
                    last.remove(connection.getId());
                } else if (event instanceof MessageReceiveEvent) {
                    Message message = ((MessageReceiveEvent) event).getMessage();
                    if (isPingMessage(message)) {
                        System.out.println("receive ping");
                        //last.put(connection.getId(), System.currentTimeMillis());
                        //connection.send(createPongMessage());
                        //concept.publish(new HeartbeatReplyEvent(connection));
                    } else if (isPongMessage(message)) {
                        last.put(connection.getId(), System.currentTimeMillis());
                    }
                }
            }
        }
    }

    public abstract void onInitialize();

    public abstract void onDestroy();

    public void sendPing() {
        Collection<Connection> connections = concept.getConnections(connectionType);
        Message message = createPingMessage();
        for (Connection connection : connections) {
            try {
                connection.send(message);
            } catch (Throwable e) {
                e.printStackTrace();
                //TODO
            }
        }
        concept.publish(new HeartbeatSendEvent(connections, connectionType));
    }

    public void closeTimeout() {
        long now = System.currentTimeMillis();
        Collection<Connection> connections = concept.getConnections(connectionType);
        for (Connection connection : connections) {
            Long l = last.get(connection.getId());
            if (l == null || now - l > timeout) {
                try {
                    concept.close(connection, "HeartbeatTimeout");
                } catch (Throwable ignore) {
                    //会发布关闭异常事件
                }
            }
        }
    }

    public boolean isPingMessage(Message message) {
        return message instanceof PingMessage;
    }

    public boolean isPongMessage(Message message) {
        return message instanceof PongMessage;
    }

    public Message createPingMessage() {
        return new BinaryPingMessage(ByteBuffer.allocate(0));
        //return new BinaryPingMessage(ByteBuffer.wrap("ping".getBytes(StandardCharsets.UTF_8)));
    }

    public Message createPongMessage() {
        return new BinaryPongMessage(ByteBuffer.allocate(0));
        //return new BinaryPongMessage(ByteBuffer.wrap("pong".getBytes(StandardCharsets.UTF_8)));
    }
}
