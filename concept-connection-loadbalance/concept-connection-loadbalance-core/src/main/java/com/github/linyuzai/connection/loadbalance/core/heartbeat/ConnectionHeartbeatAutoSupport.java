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

    private final ConnectionLoadBalanceConcept concept;

    private final String connectionType;

    private final long timeout;

    @Override
    public void onEvent(Object event) {
        if (event instanceof ConnectionLoadBalanceConceptInitializeEvent) {
            onInitialize();
        } else if (event instanceof ConnectionLoadBalanceConceptDestroyEvent) {
            onDestroy();
        } else if (event instanceof ConnectionEvent) {
            Connection connection = ((ConnectionEvent) event).getConnection();
            if (connection.getType().equals(connectionType)) {
                if (event instanceof ConnectionCloseEvent || event instanceof ConnectionCloseErrorEvent) {
                    last.remove(connection.getId());
                } else if (event instanceof MessageReceiveEvent) {
                    Message message = ((MessageReceiveEvent) event).getMessage();
                    if (isPingMessage(message)) {
                        last.put(connection.getId(), System.currentTimeMillis());
                        connection.send(createPongMessage());
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
                //TODO
            }
        }
    }

    public void closeTimeout() {
        long now = System.currentTimeMillis();
        for (Map.Entry<Object, Long> next : last.entrySet()) {
            if (now - next.getValue() > timeout) {
                try {
                    concept.close(next.getKey(), connectionType, "NoReply");
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
    }

    public Message createPongMessage() {
        return new BinaryPongMessage(ByteBuffer.allocate(0));
    }
}
