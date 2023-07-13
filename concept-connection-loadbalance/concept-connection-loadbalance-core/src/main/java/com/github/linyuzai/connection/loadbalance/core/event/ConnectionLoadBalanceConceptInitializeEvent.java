package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Concept 初始化事件。
 * <p>
 * Event will be published when concept init.
 */
@Getter
@AllArgsConstructor
public class ConnectionLoadBalanceConceptInitializeEvent extends TimestampEvent {

    private ConnectionLoadBalanceConcept concept;
}
