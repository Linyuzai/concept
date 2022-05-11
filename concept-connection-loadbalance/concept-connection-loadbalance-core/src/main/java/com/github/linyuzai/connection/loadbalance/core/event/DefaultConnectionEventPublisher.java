package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class DefaultConnectionEventPublisher implements ConnectionEventPublisher {

    private final List<ConnectionEventListener> listeners = new ArrayList<>();

    @Override
    public void publish(Object event) {
        for (ConnectionEventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Throwable e) {
                e.printStackTrace();
                //TODO
            }
        }
    }

    @Override
    public void register(Collection<? extends ConnectionEventListener> listeners) {
        this.listeners.addAll(listeners);
    }
}
