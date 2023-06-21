package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

/**
 * 事件监听器
 */
public interface ConnectionEventListener extends Scoped {

    /**
     * 事件监听回调
     *
     * @param event 事件
     */
    void onEvent(Object event);
}
