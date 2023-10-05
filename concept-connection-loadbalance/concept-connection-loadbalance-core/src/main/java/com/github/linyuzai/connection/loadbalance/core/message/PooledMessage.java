package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface PooledMessage extends Message {

    static PooledMessage wrap(Message message) {
        return new Impl(message);
    }

    Object pooled(Connection connection, Function<Message, Object> encode);

    @Getter
    @RequiredArgsConstructor
    class Impl implements PooledMessage {

        private final Map<String, Map<Class<? extends Connection>, Object>> pooled = new HashMap<>();

        private final Message message;

        @Override
        public Object pooled(Connection connection, Function<Message, Object> encode) {
            return pooled.computeIfAbsent(connection.getType(), t -> new HashMap<>())
                    .computeIfAbsent(connection.getClass(), c -> {
                        message.getHeaders().put(Message.POOLED, Boolean.TRUE.toString());
                        return encode.apply(message);
                    });
        }

        @Override
        public Map<String, String> getHeaders() {
            return message.getHeaders();
        }

        @Override
        public <T> T getPayload() {
            return message.getPayload();
        }
    }
}
