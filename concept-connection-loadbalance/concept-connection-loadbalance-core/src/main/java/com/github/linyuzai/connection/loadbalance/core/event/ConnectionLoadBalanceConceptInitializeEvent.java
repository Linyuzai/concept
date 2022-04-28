package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConnectionLoadBalanceConceptInitializeEvent {

    private ConnectionLoadBalanceConcept concept;
}
