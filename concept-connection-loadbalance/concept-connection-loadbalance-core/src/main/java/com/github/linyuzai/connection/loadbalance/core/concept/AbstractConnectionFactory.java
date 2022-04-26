package com.github.linyuzai.connection.loadbalance.core.concept;

import java.util.Map;

public abstract class AbstractConnectionFactory<Con extends Connection, Concept extends ConnectionLoadBalanceConcept> implements ConnectionFactory {

    @SuppressWarnings("unchecked")
    @Override
    public Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        return doCreate(o, metadata, (Concept) concept);
    }

    public abstract Con doCreate(Object o, Map<Object, Object> metadata, Concept concept);
}
