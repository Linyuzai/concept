package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionCloseEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEstablishEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;

/**
 * 生命周期监听器
 */
public interface LifecycleListener extends ConnectionEventListener {

    @Override
    default void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof ConnectionEstablishEvent) {
            Connection connection = ((ConnectionEstablishEvent) event).getConnection();
            if (connection.isClientType()) {
                onEstablish(connection, concept);
            }
        } else if (event instanceof ConnectionCloseEvent) {
            Connection connection = ((ConnectionCloseEvent) event).getConnection();
            if (connection.isClientType()) {
                onClose(connection, ((ConnectionCloseEvent) event).getReason(), concept);
            }
        }
    }

    /**
     * 连接建立
     *
     * @param connection 连接
     */
    void onEstablish(Connection connection, ConnectionLoadBalanceConcept concept);

    /**
     * 连接关闭
     *
     * @param connection 连接
     * @param reason     原因
     */
    void onClose(Connection connection, Object reason, ConnectionLoadBalanceConcept concept);
}
