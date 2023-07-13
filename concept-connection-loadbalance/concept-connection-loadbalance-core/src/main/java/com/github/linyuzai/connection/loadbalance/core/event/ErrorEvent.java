package com.github.linyuzai.connection.loadbalance.core.event;

/**
 * 异常事件。
 * <p>
 * Events for errors.
 */
public interface ErrorEvent {

    Throwable getError();
}
