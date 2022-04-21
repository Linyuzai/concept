package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

public interface ConnectionLoadBalanceConceptAware<T extends ConnectionLoadBalanceConcept> {

    void setConnectionLoadBalanceConcept(T concept);
}
