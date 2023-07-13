package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

/**
 * 事件监听器。
 * <p>
 * Event listener.
 */
public interface ConnectionEventListener extends Scoped {

    /**
     * 事件监听回调。
     * <p>
     * Event callback.
     */
    void onEvent(Object event, ConnectionLoadBalanceConcept concept);
}
