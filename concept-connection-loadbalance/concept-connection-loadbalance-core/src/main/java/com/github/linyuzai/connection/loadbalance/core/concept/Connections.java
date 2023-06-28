package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendInterceptor;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 组合连接
 */
@Getter
@AllArgsConstructor
public class Connections implements Connection {

    @NonNull
    private final Collection<? extends Connection> connections;

    @Override
    public Object getId() {
        return get(Connection::getId, null);
    }

    @Override
    public void setType(@NonNull String type) {
        for (Connection connection : connections) {
            connection.setType(type);
        }
    }

    @Override
    public String getType() {
        return get(Connection::getType, null);
    }

    @Override
    public Map<Object, Object> getMetadata() {
        return get(Connection::getMetadata, null);
    }

    @Override
    public void setMessageRetryStrategy(MessageRetryStrategy strategy) {
        for (Connection connection : connections) {
            connection.setMessageRetryStrategy(strategy);
        }
    }

    @Override
    public MessageRetryStrategy getMessageRetryStrategy() {
        return get(Connection::getMessageRetryStrategy, null);
    }

    @Override
    public List<MessageSendInterceptor> getMessageSendInterceptors() {
        return get(Connection::getMessageSendInterceptors, null);
    }

    @Override
    public void setMessageEncoder(MessageEncoder encoder) {
        for (Connection connection : connections) {
            connection.setMessageEncoder(encoder);
        }
    }

    @Override
    public MessageEncoder getMessageEncoder() {
        return get(Connection::getMessageEncoder, null);
    }

    @Override
    public void setMessageDecoder(MessageDecoder decoder) {
        for (Connection connection : connections) {
            connection.setMessageDecoder(decoder);
        }
    }

    @Override
    public MessageDecoder getMessageDecoder() {
        return get(Connection::getMessageDecoder, null);
    }

    @Override
    public void setConcept(@NonNull ConnectionLoadBalanceConcept concept) {
        for (Connection connection : connections) {
            connection.setConcept(concept);
        }
    }

    @Override
    public ConnectionLoadBalanceConcept getConcept() {
        return get(Connection::getConcept, null);
    }

    @Override
    public void send(@NonNull Message message) {
        for (Connection connection : connections) {
            connection.send(message);
        }
    }

    @Override
    public void send(@NonNull Message message, Runnable success, Consumer<Throwable> error) {
        for (Connection connection : connections) {
            connection.send(message, success, error);
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
        return get(Connection::isAlive, null);
    }

    @Override
    public void setAlive(boolean alive) {
        for (Connection connection : connections) {
            connection.setAlive(alive);
        }
    }

    @Override
    public long getLastHeartbeat() {
        return get(Connection::getLastHeartbeat, null);
    }

    @Override
    public void setLastHeartbeat(long lastHeartbeat) {
        for (Connection connection : connections) {
            connection.setLastHeartbeat(lastHeartbeat);
        }
    }

    protected <T> T get(Function<Connection, T> function, T empty) {
        Set<T> set = new HashSet<>();
        for (Connection connection : connections) {
            set.add(function.apply(connection));
            if (set.size() > 1) {
                throw new UnsupportedOperationException();
            }
        }
        if (set.isEmpty()) {
            return empty;
        }
        return set.iterator().next();
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
