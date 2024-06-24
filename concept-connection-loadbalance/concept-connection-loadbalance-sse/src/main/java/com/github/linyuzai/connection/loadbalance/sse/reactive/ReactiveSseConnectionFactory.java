package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionFactory;

public class ReactiveSseConnectionFactory extends SseConnectionFactory<ReactiveSseConnection, ReactiveSseCreation> {

    @Override
    protected SseConnection doCreate(ReactiveSseCreation request, ConnectionLoadBalanceConcept concept) {
        return new ReactiveSseConnection(request.getFluxSink());
    }
}
