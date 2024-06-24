package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

import java.util.Map;

/**
 * SSE 连接工厂抽象类。
 * <p>
 * Abstract class of SSE connection factory.
 */
public abstract class SseConnectionFactory<T extends SseConnection, R extends SseCreation>
        extends AbstractConnectionFactory<T> {

    public SseConnectionFactory() {
        addScopes(SseScoped.NAME);
    }

    @Override
    public boolean support(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        return o instanceof SseCreation;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected AbstractConnection doCreate(Object o, ConnectionLoadBalanceConcept concept) {
        R request = (R) o;
        SseConnection connection = doCreate(request, concept);
        connection.setCreation(request);
        return connection;
    }

    protected abstract SseConnection doCreate(R request, ConnectionLoadBalanceConcept concept);
}
