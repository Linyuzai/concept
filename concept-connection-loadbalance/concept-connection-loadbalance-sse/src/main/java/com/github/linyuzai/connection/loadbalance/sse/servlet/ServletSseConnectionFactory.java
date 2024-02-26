package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.*;

public class ServletSseConnectionFactory extends SseConnectionFactory<ServletSseConnection, ServletSseCreateRequest> {

    @Override
    protected SseConnection doCreate(ServletSseCreateRequest request, ConnectionLoadBalanceConcept concept) {
        return new ServletSseConnection(request.getSseEmitter());
    }
}
