package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class AbstractConnectionEventPublisher implements ConnectionEventPublisher {

    private final List<ConnectionEventListener> listeners;

    private final EventPublishErrorHandler errorHandler;

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

    public void handlePublishError(Object event, Throwable e) {
        //业务异常会关闭连接
        errorHandler.onEventPublishError(event, e);
    }

    @Override
    public void register(Collection<? extends ConnectionEventListener> listeners) {
        this.listeners.addAll(listeners);
    }
}
