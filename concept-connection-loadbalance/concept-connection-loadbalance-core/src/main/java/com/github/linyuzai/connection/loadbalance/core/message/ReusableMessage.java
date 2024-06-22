package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface ReusableMessage extends Message {

    static ReusableMessage create(Message message) {
        return new Impl(message);
    }

    Object reuse(Connection connection, Function<Message, Object> encode);

    @Getter
    @RequiredArgsConstructor
    class Impl implements ReusableMessage {

        private final Map<String, Map<Class<? extends Connection>, Object>> reused = new HashMap<>();

        private final Message message;

        @Override
        public Object reuse(Connection connection, Function<Message, Object> encode) {
            return reused.computeIfAbsent(connection.getType(), t -> new HashMap<>())
                    .computeIfAbsent(connection.getClass(), c -> {
                        message.getHeaders().put(Message.REUSABLE, Boolean.TRUE.toString());
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
