package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class AbstractConnectionEventPublisher implements ConnectionEventPublisher {

    private final List<ConnectionEventListener> listeners;

    @Override
    public void publish(Object event) {
        for (ConnectionEventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Throwable e) {
                handlePublishError(event, e);
            }
        }
    }

    @SneakyThrows
    public void handlePublishError(Object event, Throwable e) {
        if (event instanceof EventPublishErrorEvent) {
            throw ((EventPublishErrorEvent) event).getError();
        }
        //业务异常会关闭连接
        publish(new EventPublishErrorEvent(event, e));
    }

    @Override
    public void register(Collection<? extends ConnectionEventListener> listeners) {
        this.listeners.addAll(listeners);
    }
}
