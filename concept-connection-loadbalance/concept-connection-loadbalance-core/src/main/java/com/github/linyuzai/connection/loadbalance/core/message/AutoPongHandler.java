package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.*;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoPongHandler implements ConnectionEventListener {

    private final static long DEFAULT_TIMEOUT_AND_PERIOD = 3 * 60 * 1000;

    private final Map<Connection, Long> last = new ConcurrentHashMap<>();

    private ConnectionLoadBalanceConcept concept;

    private final ScheduledExecutorService executor;

    private final long timeout;

    private final long period;

    private volatile boolean running = true;

    public AutoPongHandler() {
        this(Executors.newSingleThreadScheduledExecutor(), DEFAULT_TIMEOUT_AND_PERIOD, DEFAULT_TIMEOUT_AND_PERIOD);
    }

    public AutoPongHandler(long timeout, long period) {
        this(Executors.newSingleThreadScheduledExecutor(), timeout, period);
    }

    public AutoPongHandler(ScheduledExecutorService executor) {
        this(executor, DEFAULT_TIMEOUT_AND_PERIOD, DEFAULT_TIMEOUT_AND_PERIOD);
    }

    public AutoPongHandler(ScheduledExecutorService executor, long timeout, long period) {
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout must > 0");
        }
        if (period <= 0) {
            throw new IllegalArgumentException("period must > 0");
        }
        this.executor = executor;
        this.timeout = timeout;
        this.period = period;
    }

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
        executor.scheduleAtFixedRate(() -> {
            while (running) {
                try {
                    long now = System.currentTimeMillis();
                    for (Map.Entry<Connection, Long> next : last.entrySet()) {
                        if (now - next.getValue() > timeout) {
                            concept.close(next.getKey(), "NoReply");
                        }
                    }
                    Collection<Connection> connections = concept.getConnections(Connection.Type.SUBSCRIBER);
                    for (Connection connection : connections) {
                        connection.send(createPingMessage());
                    }
                } catch (Throwable ignore) {
                    if (running) {
                        start();
                    }
                    break;
                }
            }
        }, 0, period, TimeUnit.MILLISECONDS);
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
