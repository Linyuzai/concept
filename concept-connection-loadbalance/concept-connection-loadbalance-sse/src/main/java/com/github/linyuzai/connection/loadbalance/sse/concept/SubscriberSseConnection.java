package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import java.util.function.Consumer;

public abstract class SubscriberSseConnection extends SseConnection {

    public SubscriberSseConnection() {
        setType(Connection.Type.SUBSCRIBER);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
    }
}
