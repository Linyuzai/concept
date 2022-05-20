package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;

/**
 * 事件发布者抽象类
 */
@Getter
@AllArgsConstructor
public class AbstractConnectionEventPublisher implements ConnectionEventPublisher {

    private final List<ConnectionEventListener> listeners;

    /**
     * 发布事件
     * <p>
     * 发布异常会被重新发布 {@link EventPublishErrorEvent} 事件
     * <p>
     * 再次异常将会直接抛出异常
     *
     * @param event 事件
     */
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
