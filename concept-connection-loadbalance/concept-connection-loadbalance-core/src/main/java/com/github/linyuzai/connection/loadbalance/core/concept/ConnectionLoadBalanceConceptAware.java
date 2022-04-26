package com.github.linyuzai.connection.loadbalance.core.concept;

public interface ConnectionLoadBalanceConceptAware<T extends ConnectionLoadBalanceConcept> {

    void setConnectionLoadBalanceConcept(T concept);
}
