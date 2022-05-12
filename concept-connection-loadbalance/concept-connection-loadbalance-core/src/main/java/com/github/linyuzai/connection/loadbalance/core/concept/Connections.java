package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.net.URI;
import java.util.*;

@Getter
@AllArgsConstructor
public class Connections implements Connection {

    @NonNull
    private final Collection<? extends Connection> connections;

    public Connection get() {
        if (connections.size() == 1) {
            return connections.iterator().next();
        }
        return null;
    }

    @Override
    public Object getId() {
        Connection connection = get();
        return connection == null ? null : connection.getId();
    }

    @Override
    public String getType() {
        Connection connection = get();
        return connection == null ? null : connection.getType();
    }

    @Override
    public URI getUri() {
        Connection connection = get();
        return connection == null ? null : connection.getUri();
    }

    @Override
    public Map<Object, Object> getMetadata() {
        Connection connection = get();
        return connection == null ? Collections.emptyMap() : connection.getMetadata();
    }

    @Override
    public MessageEncoder getMessageEncoder() {
        Connection connection = get();
        return connection == null ? null : connection.getMessageEncoder();
    }

    @Override
    public MessageDecoder getMessageDecoder() {
        Connection connection = get();
        return connection == null ? null : connection.getMessageDecoder();
    }

    @Override
    public ConnectionLoadBalanceConcept getConcept() {
        ConnectionLoadBalanceConcept concept = null;
        for (Connection connection : connections) {
            if (concept == null) {
                concept = connection.getConcept();
            } else {
                if (concept != connection.getConcept()) {
                    return null;
                }
            }
        }
        return concept;
    }

    @Override
    public void redefineType(String type, Redefiner redefiner) {
        for (Connection connection : connections) {
            connection.redefineType(type, redefiner);
        }
    }

    @Override
    public void send(Message message) {
        for (Connection connection : connections) {
            connection.send(message);
        }
    }

    @Override
    public void close() {
        connections.forEach(Connection::close);
    }

    @Override
    public boolean isAlive() {
        Set<Boolean> set = new HashSet<>();
        for (Connection connection : connections) {
            set.add(connection.isAlive());
            if (set.size() > 1) {
                throw new UnsupportedOperationException();
            }
        }
        if (set.isEmpty()) {
            return false;
        }
        return set.iterator().next();
    }

    @Override
    public void setAlive(boolean alive) {
        for (Connection connection : connections) {
            connection.setAlive(alive);
        }
    }

    @Override
    public long getLastHeartbeat() {
        return -1;
    }

    @Override
    public void setLastHeartbeat(long lastHeartbeat) {
        for (Connection connection : connections) {
            connection.setLastHeartbeat(lastHeartbeat);
        }
    }

    @SafeVarargs
    public static Connection of(Collection<? extends Connection>... connections) {
        if (connections == null || connections.length == 0) {
            return null;
        }
        Collection<Connection> combine = new ArrayList<>();
        for (Collection<? extends Connection> cons : connections) {
            if (cons == null || cons.isEmpty()) {
                continue;
            }
            combine.addAll(cons);
        }
        if (combine.isEmpty()) {
            return null;
        } else if (combine.size() == 1) {
            return combine.iterator().next();
        } else {
            return new Connections(combine);
        }
    }
}
