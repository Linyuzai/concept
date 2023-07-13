package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Concept 销毁事件。
 * <p>
 * Event will be published when concept destroyed.
 */
@Getter
@AllArgsConstructor
public class ConnectionLoadBalanceConceptDestroyEvent extends TimestampEvent {

    private ConnectionLoadBalanceConcept concept;
}
