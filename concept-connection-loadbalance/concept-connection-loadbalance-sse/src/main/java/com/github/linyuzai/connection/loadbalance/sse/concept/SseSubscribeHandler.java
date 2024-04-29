package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEstablishEvent;

/**
 * SSE 负载均衡自动订阅。
 * <p>
 * Auto subscriber for SSE load balance.
 */
public class SseSubscribeHandler implements SseEventListener {

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof ConnectionEstablishEvent) {
            Connection connection = ((ConnectionEstablishEvent) event).getConnection();
            if (connection.isObservableType()) {
                //触发全量订阅
                //Trigger full subscription
                concept.getConnectionSubscriber().subscribe();
            }
        }
    }
}
