package com.github.linyuzai.connection.loadbalance.core.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageIdempotentVerifierImpl implements MessageIdempotentVerifier {

    private final Map<String, Boolean> ids = new ConcurrentHashMap<>();

    @Override
    public boolean verify(Message message) {
        String id = message.getId();
        if (id == null) {
            return true;
        }
        Holder holder = new Holder();
        ids.computeIfAbsent(id, k -> {
            holder.verified = true;
            return Boolean.TRUE;
        });
        return holder.verified;
    }

    static class Holder {

        boolean verified = false;
    }
}
