package com.github.linyuzai.connection.loadbalance.core.event;

/**
 * 异常事件
 */
public interface ErrorEvent {

    Throwable getError();
}
