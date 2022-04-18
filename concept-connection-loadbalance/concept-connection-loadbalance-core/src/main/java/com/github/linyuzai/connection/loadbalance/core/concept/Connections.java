package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Connections implements Connection {

    private Collection<? extends Connection> connections;

    @Override
    public Object getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getMetadata() {
        throw new UnsupportedOperationException();
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
}
