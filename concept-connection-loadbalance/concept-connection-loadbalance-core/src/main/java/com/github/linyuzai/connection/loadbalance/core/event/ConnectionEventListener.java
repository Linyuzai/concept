package com.github.linyuzai.connection.loadbalance.core.event;

/**
 * 事件监听器
 */
public interface ConnectionEventListener {

    /**
     * 事件监听回调
     *
     * @param event 事件
     */
    void onEvent(Object event);
}
