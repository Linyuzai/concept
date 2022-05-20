package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

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
    public void setType(String type) {
        for (Connection connection : connections) {
            connection.setType(type);
        }
    }

    @Override
    public String getType() {
        Connection connection = get();
        return connection == null ? null : connection.getType();
    }

    @Override
    public Map<Object, Object> getMetadata() {
        Connection connection = get();
        return connection == null ? Collections.emptyMap() : connection.getMetadata();
    }

    @Override
    public void setMessageEncoder(MessageEncoder encoder) {
        for (Connection connection : connections) {
            connection.setMessageEncoder(encoder);
        }
    }

    @Override
    public MessageEncoder getMessageEncoder() {
        Connection connection = get();
        return connection == null ? null : connection.getMessageEncoder();
    }

    @Override
    public void setMessageDecoder(MessageDecoder decoder) {
        for (Connection connection : connections) {
            connection.setMessageDecoder(decoder);
        }
    }

    @Override
    public MessageDecoder getMessageDecoder() {
        Connection connection = get();
        return connection == null ? null : connection.getMessageDecoder();
    }

    @Override
    public void setConcept(ConnectionLoadBalanceConcept concept) {
        for (Connection connection : connections) {
            connection.setConcept(concept);
        }
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
    public void close(String reason) {
        for (Connection connection : connections) {
            connection.close(reason);
        }
    }

    @Override
    public void close(int code, String reason) {
        for (Connection connection : connections) {
            connection.close(code, reason);
        }
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
