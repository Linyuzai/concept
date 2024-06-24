package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionFactory;

public class ServletSseConnectionFactory extends SseConnectionFactory<ServletSseConnection, ServletSseCreation> {

    @Override
    protected SseConnection doCreate(ServletSseCreation request, ConnectionLoadBalanceConcept concept) {
        return new ServletSseConnection(request.getSseEmitter());
    }
}
