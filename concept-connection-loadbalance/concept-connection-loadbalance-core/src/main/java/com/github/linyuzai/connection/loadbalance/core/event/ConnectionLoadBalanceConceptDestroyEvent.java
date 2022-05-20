package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@link ConnectionLoadBalanceConcept} 销毁事件
 */
@Getter
@AllArgsConstructor
public class ConnectionLoadBalanceConceptDestroyEvent {

    private ConnectionLoadBalanceConcept concept;
}
