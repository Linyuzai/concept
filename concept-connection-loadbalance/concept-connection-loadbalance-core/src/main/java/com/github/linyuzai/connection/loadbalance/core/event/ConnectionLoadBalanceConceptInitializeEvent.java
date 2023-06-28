package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@link ConnectionLoadBalanceConcept} 初始化事件
 */
@Getter
@AllArgsConstructor
public class ConnectionLoadBalanceConceptInitializeEvent extends TimestampEvent {

    private ConnectionLoadBalanceConcept concept;
}
