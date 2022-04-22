package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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
    public Map<Object, Object> getMetadata() {
        Connection connection = get();
        return connection == null ? null : connection.getMetadata();
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
    public void send(Message message) {
        for (Connection connection : connections) {
            try {
                connection.send(message);
            } catch (Throwable ignore) {

            }
        }
    }

    @Override
    public void close() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (Throwable ignore) {
            }
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
