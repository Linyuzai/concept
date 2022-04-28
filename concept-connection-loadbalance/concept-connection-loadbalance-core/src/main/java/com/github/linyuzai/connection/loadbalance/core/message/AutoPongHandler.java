package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.*;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoPongHandler implements ConnectionEventListener {

    private final Map<Connection, Long> last = new ConcurrentHashMap<>();

    private ConnectionLoadBalanceConcept concept;

    private volatile boolean running = true;

    @Override
    public void onEvent(Object event) {
        if (event instanceof ConnectionLoadBalanceConceptInitializeEvent) {
            this.concept = ((ConnectionLoadBalanceConceptInitializeEvent) event).getConcept();
            start();
        } else if (event instanceof ConnectionLoadBalanceConceptDestroyEvent) {
            stop();
        } else if (event instanceof Message) {
            if (event instanceof ConnectionCloseEvent) {
                last.remove(((ConnectionCloseEvent) event).getConnection());
            } else if (isPingMessage((Message) event)) {
                Connection connection = ((PongMessageEvent) event).getConnection();
                connection.send(createPongMessage());
                last.put(connection, System.currentTimeMillis());
            }
        }
    }

    public void start() {
        new Thread(() -> {
            while (running) {
                try {
                    long now = System.currentTimeMillis();
                    for (Map.Entry<Connection, Long> next : last.entrySet()) {
                        if (now - next.getValue() > 3 * 60 * 1000) {
                            concept.close(next.getKey(), "NoReply");
                        }
                    }
                    Collection<Connection> connections = concept.getConnections(Connection.Type.SUBSCRIBER);
                    for (Connection connection : connections) {
                        connection.send(createPingMessage());
                    }
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException ignore) {
                    if (running) {
                        start();
                    }
                    break;
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
    }

    public boolean isPingMessage(Message message) {
        if (message instanceof PingMessage) {
            return true;
        }
        if (message instanceof BinaryMessage) {
            ByteBuffer payload = message.getPayload();
            return payload.hasArray() && payload.array().length == 0;
        }
        return false;
    }

    public Message createPingMessage() {
        return new BinaryPingMessage(ByteBuffer.allocate(0));
    }

    public Message createPongMessage() {
        return new BinaryPongMessage(ByteBuffer.allocate(0));
    }
}
